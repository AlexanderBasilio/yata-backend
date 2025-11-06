package com.yata.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId; // UUID generado

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // ===== ITEMS (JSON) =====
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // ===== LOCATION =====
    @Embedded
    private Location location;

    // ===== PAYMENT =====
    @Embedded
    private Payment payment;

    // ===== SUMMARY =====
    @Embedded
    private Summary summary;

    // ===== EXTRAS =====
    @Column(length = 500)
    private String deliveryNote;

    @Column(nullable = false)
    private String status; // "PENDING", "CONFIRMED", "DELIVERED", "CANCELLED"

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "points_earned")
    private Integer pointsEarned;
}