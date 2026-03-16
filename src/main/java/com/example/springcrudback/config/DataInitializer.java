package com.example.springcrudback.config;

import com.example.springcrudback.user.Role;
import com.example.springcrudback.user.UserAccount;
import com.example.springcrudback.user.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserAccountRepository userAccountRepository,
                           PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userAccountRepository.existsByUsername("user1")) {
            userAccountRepository.save(
                    new UserAccount(
                            "user1",
                            passwordEncoder.encode("00000000"),
                            Role.USER
                    )
            );
        }
    }
}