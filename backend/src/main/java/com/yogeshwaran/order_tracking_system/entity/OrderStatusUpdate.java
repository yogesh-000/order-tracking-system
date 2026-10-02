package com.yogeshwaran.order_tracking_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderStatusUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime changedAt = LocalDateTime.now();
    private String note;
}
