package com.yogeshwaran.order_tracking_system.controller;

import com.yogeshwaran.order_tracking_system.dto.cart.CartResponse;
import com.yogeshwaran.order_tracking_system.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items/{productId}")
    public CartResponse addItem(@PathVariable Long productId) {
        return cartService.addItem(productId);
    }

    @DeleteMapping("/items/{productId}")
    public CartResponse removeItem(@PathVariable Long productId) {
        return cartService.removeItem(productId);
    }

    @PutMapping("/items/{productId}/decrease")
    public CartResponse decreaseItem(@PathVariable Long productId) {
        return cartService.decreaseItem(productId);
    }
}
