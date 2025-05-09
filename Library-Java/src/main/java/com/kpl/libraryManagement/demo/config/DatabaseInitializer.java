package com.kpl.libraryManagement.demo.config;

import com.kpl.libraryManagement.demo.model.Role;
import com.kpl.libraryManagement.demo.model.User;
import com.kpl.libraryManagement.demo.repository.RoleRepository;
import com.kpl.libraryManagement.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner{


        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
            initRoles();
            createAdminUserIfNotExists();
        }

        private void initRoles() {
            if (roleRepository.count() == 0) {
                Role clientRole = new Role();
                clientRole.setName(Role.ERole.ROLE_CLIENT);
                roleRepository.save(clientRole);

                Role adminRole = new Role();
                adminRole.setName(Role.ERole.ROLE_ADMIN);
                roleRepository.save(adminRole);
            }
        }

        private void createAdminUserIfNotExists() {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));

                Set<Role> roles = new HashSet<>();
                Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
                roles.add(adminRole);
                admin.setRoles(roles);

                userRepository.save(admin);
            }

    }
}
