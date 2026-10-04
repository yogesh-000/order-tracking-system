package com.yogeshwaran.order_tracking_system.service;

import com.yogeshwaran.order_tracking_system.dto.cart.CartResponse;

public interface CartService {
    CartResponse getCart();
    CartResponse addItem(Long productId);
    CartResponse removeItem(Long productId);
    CartResponse decreaseItem(Long productId);
}
