package com.bt.cricketacademy.batch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {
    @GetMapping("/batches/hello")
    public String hello() {
        return "Hello from Batch Service!";
    }
}

