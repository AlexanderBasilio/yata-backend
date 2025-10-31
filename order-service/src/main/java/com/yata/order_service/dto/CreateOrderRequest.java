package com.yata.order_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {

    private List<OrderItemDTO> items;
    private LocationDTO location;
    private PaymentDTO payment;
    private SummaryDTO summary;
    private String deliveryNote;
    private String customerName;
    private String customerPhone;

    @Data
    public static class OrderItemDTO {
        private String productId;
        private String name;
        private Double price;
        private Integer quantity;
        private String unit;
    }

    @Data
    public static class LocationDTO {
        private String address;
        private Double latitude;
        private Double longitude;
        private String city;
        private String region;
    }

    @Data
    public static class PaymentDTO {
        private String method;
        private Double cashAmount;
        private Double changeAmount;
    }

    @Data
    public static class SummaryDTO {
        private Double subtotal;
        private Double serviceCost;
        private Double shippingCost;
        private Double total;
    }
}