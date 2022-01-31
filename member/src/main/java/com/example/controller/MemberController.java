package com.example.controller;

import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final OrderService orderService;

    @Value("${server.port}")
    private int port;

    @GetMapping("/welcome")
    public String welcome() {
        log.info("member welcome api. port : {} ", port);
        return "Welcome to member service";
    }

    @GetMapping("/{memberId}")
    public HashMap<String, String> getMember(@PathVariable String memberId) {
        String order = orderService.getOrderByMemberId(memberId);

        log.info("member getMember api. port : {} ", port);
        HashMap<String, String> response = new HashMap<>();
        response.put("memberId", memberId);
        response.put("order", order);

        return response;
    }
}
