package com.yogeshwaran.order_tracking_system.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter @AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String name;
    private BigDecimal price;
    private int quantity;
}