package com.example.service;

import com.example.client.OrderServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HystrixService {
    private final RestTemplate restTemplate;
    private static final String ORDER_URL = "http://order-service/%s";

    public String getOrders(String orderId) {
        return restTemplate.getForObject(String.format(ORDER_URL, orderId),
                String.class);
    }

    @HystrixCommand
    public String getOrdersV2(String orderId) {
        return restTemplate.getForObject(String.format(ORDER_URL, orderId),
                String.class);
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String getOrderError(String orderId) {
        return restTemplate.getForObject(String.format(ORDER_URL + "/error", orderId),
                String.class);
    }

    public String fallback(String orderId, Throwable t) {
        System.out.println("t = " + t);
        return "fallback method";
    }
}
