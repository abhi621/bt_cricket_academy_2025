package com.bt.cricketacademy.student.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @GetMapping("/students/hello")
    public String hello() {
        return "Hello from Student Service!";
    }
}
