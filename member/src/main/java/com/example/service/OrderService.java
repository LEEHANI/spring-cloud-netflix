package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RestTemplate restTemplate;
//    private static final String ORDER_URL = "http://localhost:8000/order-service/%s/orders";
private static final String ORDER_URL = "http://order-service/%s/orders";

    public String getOrderByMemberId(String memberId) {
        return restTemplate.getForObject(String.format(ORDER_URL, memberId), String.class);
    }
}
