package com.yogeshwaran.order_tracking_system.util;

import com.yogeshwaran.order_tracking_system.dto.cart.CartItemResponse;
import com.yogeshwaran.order_tracking_system.dto.cart.CartResponse;
import com.yogeshwaran.order_tracking_system.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public class CartMapper {
    public static CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(i -> new CartItemResponse(i.getProduct().getId(), i.getProduct().getName(),
                        i.getProduct().getPrice(), i.getQuantity()))
                .toList();

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, total);
    }
}
