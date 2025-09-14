package com.bt.cricketacademy.fee.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeeController {
    @GetMapping("/fees/hello")
    public String hello() {
        return "Hello from Fee Service!";
    }
}

