package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class OrderController {

    @GetMapping("{memberId}/orders")
    public String getOrderByMemberId(String memberId) {
        String order = UUID.randomUUID().toString();

        log.info("random order: {}", order);

        return order;
    }
}
