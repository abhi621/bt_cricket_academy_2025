package com.bt.cricketacademy.auth.controller;

import com.bt.cricketacademy.auth.dto.*;
import com.bt.cricketacademy.auth.model.AppUser;
import com.bt.cricketacademy.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth/hello")
    public String hello() {
        return "Hello from Auth Service!";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        
        if (response.getToken() == null) {
            // Login failed
            if (response.getMessage().contains("not approved")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        
        if (response.getMessage().contains("already exists")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/auth/approve/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<AppUser> approveUser(@PathVariable Long userId) {
        AppUser approvedUser = authService.approveUser(userId);
        
        if (approvedUser == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(approvedUser);
    }

    @GetMapping("/auth/pending-approvals")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<List<AppUser>> getPendingApprovals() {
        List<AppUser> pendingUsers = authService.getPendingApprovals();
        return ResponseEntity.ok(pendingUsers);
    }
}

