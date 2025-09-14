package com.bt.cricketacademy.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bt.cricketacademy.auth.model.AppUser;
import com.bt.cricketacademy.auth.repository.AppUserRepository;

@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedDefaultUsers(AppUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            seedUser(repo, encoder, "admin", "admin123", "admin@btca.local", "ADMIN", true, true);
            seedUser(repo, encoder, "support", "support123", "support@btca.local", "SUPPORT", true, true);
            seedUser(repo, encoder, "coach", "coach123", "coach@btca.local", "COACH", true, true);
            seedUser(repo, encoder, "player", "player123", "player@btca.local", "PLAYER", true, true);
        };
    }

    private void seedUser(AppUserRepository repo, PasswordEncoder encoder,
                          String username, String rawPassword, String email,
                          String role, boolean active, boolean approved) {
        if (repo.findByUsername(username) != null) {
            return; // already exists
        }
        AppUser u = new AppUser();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setEmail(email);
        u.setRole(role);
        u.setActive(active);
        u.setApproved(approved);
        repo.save(u);
        System.out.println("[SEED] Created user: " + username + " role=" + role);
    }
}
