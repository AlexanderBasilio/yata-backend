package com.yata.order_service.controller;

import com.yata.order_service.dto.CreateOrderRequest;
import com.yata.order_service.dto.CreateOrderResponse;
import com.yata.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody CreateOrderRequest request) {

        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }
}