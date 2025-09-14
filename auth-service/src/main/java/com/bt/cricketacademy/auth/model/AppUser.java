package com.bt.cricketacademy.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String role; // ADMIN, SUPPORT, COACH, PLAYER
    
    @Column
    private String name;
    
    @Column
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING; // PENDING, ACTIVE, INACTIVE
    
    // Legacy fields for backward compatibility
    private boolean active = false;
    private boolean approved = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime approvedAt;
    
    @Column
    private Long approvedBy; // ID of admin who approved
    
    public enum UserStatus {
        PENDING, ACTIVE, INACTIVE
    }
    
    // Default constructor
    public AppUser() {}
    
    // Constructor
    public AppUser(String username, String password, String email, String role, String name, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.name = name;
        this.phone = phone;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { 
        this.status = status;
        // Update legacy fields for backward compatibility
        this.active = (status == UserStatus.ACTIVE);
        this.approved = (status == UserStatus.ACTIVE);
    }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { 
        this.active = active;
        if (active && status == UserStatus.PENDING) {
            this.status = UserStatus.ACTIVE;
        }
    }
    
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { 
        this.approved = approved;
        if (approved && status == UserStatus.PENDING) {
            this.status = UserStatus.ACTIVE;
        }
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    
    @PreUpdate
    protected void onUpdate() {
        // Sync legacy fields with status
        this.active = (this.status == UserStatus.ACTIVE);
        this.approved = (this.status == UserStatus.ACTIVE);
    }
}

