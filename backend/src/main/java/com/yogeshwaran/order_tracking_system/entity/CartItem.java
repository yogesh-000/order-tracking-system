package com.yogeshwaran.order_tracking_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private int quantity;
}
