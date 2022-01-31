package com.example.controller;

import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final OrderService orderService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to member service";
    }

    @GetMapping("/{memberId}")
    public HashMap<String, String> getMember(@PathVariable String memberId) {
        String order = orderService.getOrderByMemberId(memberId);

        HashMap<String, String> response = new HashMap<>();
        response.put("memberId", memberId);
        response.put("order", order);

        return response;
    }
}
