package com.yata.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateOrderResponse {
    private boolean success;
    private String orderId;
    private String message;
    private Integer pointsEarned;
}