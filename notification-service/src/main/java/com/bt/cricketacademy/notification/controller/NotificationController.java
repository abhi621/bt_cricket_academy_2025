package com.bt.cricketacademy.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @GetMapping("/notifications/hello")
    public String hello() {
        return "Hello from Notification Service!";
    }
}

