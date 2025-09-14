package com.bt.cricketacademy.auth.service;

import com.bt.cricketacademy.auth.dto.*;
import com.bt.cricketacademy.auth.model.AppUser;
import com.bt.cricketacademy.auth.repository.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository userRepository;

    @Value("${JWT_SECRET:changeme}")
    private String jwtSecret;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse login(LoginRequest loginRequest) {
        AppUser user = userRepository.findByUsername(loginRequest.getUsername());
        
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid credentials");
        }

        if (!user.isApproved()) {
            return new AuthResponse("Your account is not approved yet. Please contact academy staff.");
        }

        String token = generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole(), user.isApproved());
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if username or email already exists
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return new AuthResponse("Username already exists");
        }
        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            return new AuthResponse("Email already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole());
        
        // For self-registration (COACH/PLAYER), set approved=false
        if ("COACH".equals(registerRequest.getRole()) || "PLAYER".equals(registerRequest.getRole())) {
            user.setApproved(false);
            user.setActive(false);
        } else {
            // Admin/Support can register with approved=true
            user.setApproved(true);
            user.setActive(true);
        }

        userRepository.save(user);
        
        // Log notification for pending approval (can be enhanced with actual notification service)
        if (!user.isApproved()) {
            System.out.println("New registration pending approval: " + user.getUsername() + " (" + user.getEmail() + ")");
        }

        return new AuthResponse("Registration successful. " + 
            (user.isApproved() ? "You can now login." : "Your registration is pending approval. You will be notified once approved."));
    }

    public AppUser approveUser(Long userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setApproved(true);
            user.setActive(true);
            userRepository.save(user);
            System.out.println("User approved: " + user.getUsername() + " (" + user.getEmail() + ")");
        }
        return user;
    }

    public List<AppUser> getPendingApprovals() {
        return userRepository.findByApprovedFalse();
    }

    private String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }
}