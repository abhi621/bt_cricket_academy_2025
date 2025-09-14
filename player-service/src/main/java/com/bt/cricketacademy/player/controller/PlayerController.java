package com.bt.cricketacademy.player.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    @GetMapping("/players/hello")
    public String hello() {
        return "Hello from Player Service!";
    }
}

