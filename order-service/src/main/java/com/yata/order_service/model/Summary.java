package com.yata.order_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Summary {

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double serviceCost;

    @Column(nullable = false)
    private Double shippingCost;

    @Column(nullable = false)
    private Double total;
}