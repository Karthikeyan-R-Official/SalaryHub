package com.salaryhub.config;

import com.salaryhub.entity.AppUser;
import com.salaryhub.entity.Role;
import com.salaryhub.repository.AppUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HRManagerInitializer implements ApplicationRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public HRManagerInitializer(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            Environment environment) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {

        String username = environment.getProperty("HR_MANAGER_USERNAME");

        if (username == null || username.isBlank()) {
            throw new IllegalStateException(
                    "HR_MANAGER_USERNAME environment variable is required."
            );
        }

        username = username.trim();

        if (appUserRepository.existsByUsername(username)) {
            System.out.println("HR Manager account already exists.");
            return;
        }

        String password = environment.getProperty("HR_MANAGER_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "HR_MANAGER_PASSWORD environment variable is required."
            );
        }

        AppUser hrManager = new AppUser(
                username,
                passwordEncoder.encode(password),
                Role.HR_MANAGER
        );

        appUserRepository.save(hrManager);

        System.out.println("Initial HR Manager account created successfully.");
    }
}