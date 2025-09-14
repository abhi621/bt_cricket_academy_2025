package com.bt.cricketacademy.auth.dto;

public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private boolean approved;
    private String message;

    // constructors
    public AuthResponse() {}

    public AuthResponse(String token, String username, String role, boolean approved) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.approved = approved;
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    // getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}