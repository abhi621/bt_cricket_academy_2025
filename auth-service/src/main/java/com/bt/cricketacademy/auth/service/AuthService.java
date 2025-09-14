package com.bt.cricketacademy.auth.service;

import com.bt.cricketacademy.auth.dto.*;
import com.bt.cricketacademy.auth.model.AppUser;
import com.bt.cricketacademy.auth.repository.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Key;
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

        if (user.getStatus() != AppUser.UserStatus.ACTIVE) {
            return new AuthResponse("Your account is not approved yet. Please contact academy staff.");
        }

        String token = generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole(), user.getStatus() == AppUser.UserStatus.ACTIVE);
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
        user.setName(registerRequest.getName() != null ? registerRequest.getName() : registerRequest.getUsername());
        user.setPhone(registerRequest.getPhone());
        
        // For self-registration (COACH/PLAYER), set status=PENDING
        if ("COACH".equals(registerRequest.getRole()) || "PLAYER".equals(registerRequest.getRole())) {
            user.setStatus(AppUser.UserStatus.PENDING);
        } else {
            // Admin/Support can register with status=ACTIVE
            user.setStatus(AppUser.UserStatus.ACTIVE);
        }

        userRepository.save(user);
        
        // Log notification for pending approval (can be enhanced with actual notification service)
        if (user.getStatus() == AppUser.UserStatus.PENDING) {
            System.out.println("New registration pending approval: " + user.getUsername() + " (" + user.getEmail() + ")");
        }

        return new AuthResponse("Registration successful. " + 
            (user.getStatus() == AppUser.UserStatus.ACTIVE ? "You can now login." : "Your registration is pending approval. You will be notified once approved."));
    }

    public AppUser approveUser(Long userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setStatus(AppUser.UserStatus.ACTIVE);
            user.setApprovedAt(java.time.LocalDateTime.now());
            // TODO: Set approvedBy to current admin's ID when security context is available
            userRepository.save(user);
            System.out.println("User approved: " + user.getUsername() + " (" + user.getEmail() + ")");
        }
        return user;
    }

    public List<AppUser> getPendingApprovals() {
        // Find users with PENDING status
        return userRepository.findByStatus(AppUser.UserStatus.PENDING);
    }

    private String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key signingKey() {
        try {
            byte[] keyBytes = MessageDigest.getInstance("SHA-256").digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}