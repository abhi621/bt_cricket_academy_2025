package com.bt.cricketacademy.auth.repository;

import com.bt.cricketacademy.auth.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
    
    // Legacy method for backward compatibility
    List<AppUser> findByApprovedFalse();
    
    // New method using status
    List<AppUser> findByStatus(AppUser.UserStatus status);
    
    // Find by role
    List<AppUser> findByRole(String role);
    
    // Find by role and status
    List<AppUser> findByRoleAndStatus(String role, AppUser.UserStatus status);
}

