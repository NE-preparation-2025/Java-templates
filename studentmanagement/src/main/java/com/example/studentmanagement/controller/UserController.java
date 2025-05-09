package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.UserDto;
import com.example.studentmanagement.model.User;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(user.getRoles().stream()
                            .map(role -> role.getName().name())
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(user.getRoles().stream()
                            .map(role -> role.getName().name())
                            .collect(Collectors.toList()));
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/auth/user")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String token) {
        // This endpoint would normally use the JWT token to identify the current user
        // For simplicity, we're just returning a mock user
        // In a real implementation, you would extract the username from the token and look up the user

        UserDto mockUser = new UserDto();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setEmail("admin@example.com");
        mockUser.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));

        return ResponseEntity.ok(mockUser);
    }
}
