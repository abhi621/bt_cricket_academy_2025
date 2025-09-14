package com.bt.cricketacademy.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @GetMapping("/auth/hello")
    public String hello() {
        return "Hello from Auth Service!";
    }
}

