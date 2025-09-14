package com.bt.cricketacademy.coach.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoachController {
    @GetMapping("/coaches/hello")
    public String hello() {
        return "Hello from Coach Service!";
    }
}

