package com.bt.cricketacademy.auth.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ImpersonationController {
    @Value("${JWT_SECRET:changeme}")
    private String jwtSecret;

    @GetMapping("/auth/impersonate")
    public ResponseEntity<?> impersonate(@RequestParam String role) {
        // Validate role
        if (!role.matches("ROLE_(ADMIN|SUPPORT|COACH|PLAYER)")) {
            return ResponseEntity.badRequest().body("Invalid role");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject("impersonated-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
        return ResponseEntity.ok(Map.of("token", token));
    }
}

