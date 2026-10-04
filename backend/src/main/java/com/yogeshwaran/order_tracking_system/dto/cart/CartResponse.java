package com.yogeshwaran.order_tracking_system.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

@Getter @AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal total;
}
