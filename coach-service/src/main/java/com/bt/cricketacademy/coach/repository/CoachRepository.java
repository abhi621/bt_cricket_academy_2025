package com.bt.cricketacademy.coach.repository;

import com.bt.cricketacademy.coach.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach, Long> {
}

