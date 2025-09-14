package com.bt.cricketacademy.auth.repository;

import com.bt.cricketacademy.auth.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}

