package com.bt.cricketacademy.payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @GetMapping("/payments/hello")
    public String hello() {
        return "Hello from Payment Service!";
    }
}
