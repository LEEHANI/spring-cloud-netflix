package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EurekaClientController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to member service";
    }
}
