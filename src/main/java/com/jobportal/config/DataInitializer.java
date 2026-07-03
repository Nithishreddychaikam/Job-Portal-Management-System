package com.jobportal.config;

import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default Admin if it doesn't exist
        if (userRepository.findByEmail("admin@jobportal.com").isEmpty()) {
            User admin = User.builder()
                    .name("System Administrator")
                    .email("admin@jobportal.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .isVerified(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println("Default Admin User Initialized successfully.");
        }
    }
}
