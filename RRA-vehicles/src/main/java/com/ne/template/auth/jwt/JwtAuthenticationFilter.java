package com.ne.template.auth.jwt;

import com.ne.template.auth.exceptions.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher; // Import AntPathMatcher

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final String[] authWhitelist; // Add field for whitelist

    private final AntPathMatcher pathMatcher = new AntPathMatcher(); // Add AntPathMatcher

    // Modify constructor to accept whitelist
    public JwtAuthenticationFilter(
            JwtService jwtService,
            AuthenticationEntryPoint authenticationEntryPoint,
            @Value("${security.auth-whitelist}") String[] authWhitelist // Inject from properties or pass from SecurityConfig
    ) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authWhitelist = authWhitelist;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();

        // Check if the request path is in the whitelist using AntPathMatcher
        boolean isWhitelisted = false;
        for (String whitelistedPath : authWhitelist) {
            if (pathMatcher.match(whitelistedPath, requestPath)) {
                isWhitelisted = true;
                break;
            }
        }

        // If the path is whitelisted, bypass token processing
        if (isWhitelisted) {
            log.debug("Bypassing JWT filter for whitelisted path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        // Validate Authorization header (only for non-whitelisted paths)
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            log.debug("No JWT token found in request for non-whitelisted path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Parse token
            String token = authHeader.substring(7);
            Jwt jwt = jwtService.parseToken(token);

            // Validate claims
            if (jwt.getUserId() == null || jwt.getRole() == null) {
                throw new InvalidJwtException("Missing token claims");
            }

            // Set authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            jwt.getUserId(),
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("" + jwt.getRole()))
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Successfully authenticated user: {}", jwt.getUserId());
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            log.debug("Authentication failed for {}: {}", requestPath, ex.getMessage());
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InvalidJwtException(ex.getMessage()) // Pass the original exception message
            );
        }
    }
}