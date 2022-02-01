package com.example.service;

import com.example.client.OrderServiceClient;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
//    private static final String ORDER_URL = "http://localhost:8000/order-service/%s/orders";
    private static final String ORDER_URL = "http://order-service/%s/orders";

    public String getOrderByMemberId(String memberId) {
        return restTemplate.getForObject(String.format(ORDER_URL, memberId), String.class);
    }

    public String getOrderByMemberIdUseFeign(String memberId) {
        return orderServiceClient.getOrderByMemberId(memberId);
    }
}
