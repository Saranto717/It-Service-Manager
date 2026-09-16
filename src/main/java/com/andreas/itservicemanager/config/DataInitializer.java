package com.andreas.itservicemanager.config;

import com.andreas.itservicemanager.entity.Role;
import com.andreas.itservicemanager.entity.User;
import com.andreas.itservicemanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail("admin@itservice.com")) {

                User admin = new User(
                        "System",
                        "Admin",
                        "admin@itservice.com",
                        passwordEncoder.encode("Admin123!"),
                        Role.ADMIN
                );

                userRepository.save(admin);

                System.out.println("Default ADMIN user created.");
            }
        };
    }
}