package com.bt.cricketacademy.player.repository;

import com.bt.cricketacademy.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

