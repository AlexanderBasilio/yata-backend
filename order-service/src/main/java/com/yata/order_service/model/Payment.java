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
public class Payment {

    @Column(nullable = false)
    private String method; // "exact" | "cash"

    private Double cashAmount;
    private Double changeAmount;
}