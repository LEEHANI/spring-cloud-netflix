package com.example.controller;

import com.example.service.HystrixService;
import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HystrixController {

    private final HystrixService hystrixService;

    @GetMapping("/{memberId}")
    public String getOrders(String memberId) {
        return hystrixService.getOrders(memberId);
    }

    @GetMapping("/{memberId}/hystrix")
    public String getOrderV2(String memberId) {
        return hystrixService.getOrdersV2(memberId);
    }

    @GetMapping("/{memberId}/error")
    public String getOrderError(String memberId) {
        return hystrixService.getOrderError(memberId);
    }
}
