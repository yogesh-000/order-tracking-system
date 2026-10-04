package com.yogeshwaran.order_tracking_system.service.impl;

import com.yogeshwaran.order_tracking_system.dto.cart.CartResponse;
import com.yogeshwaran.order_tracking_system.entity.Cart;
import com.yogeshwaran.order_tracking_system.entity.CartItem;
import com.yogeshwaran.order_tracking_system.entity.Product;
import com.yogeshwaran.order_tracking_system.entity.User;
import com.yogeshwaran.order_tracking_system.exception.BusinessValidationException;
import com.yogeshwaran.order_tracking_system.exception.ResourceNotFoundException;
import com.yogeshwaran.order_tracking_system.repository.CartRepository;
import com.yogeshwaran.order_tracking_system.repository.ProductRepository;
import com.yogeshwaran.order_tracking_system.security.SecurityUtils;
import com.yogeshwaran.order_tracking_system.service.CartService;
import com.yogeshwaran.order_tracking_system.util.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final SecurityUtils securityUtils;

    private Cart getOrCreateCart(User customer) {
        return cartRepository.findByCustomer_Id(customer.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setCustomer(customer);
                    return cartRepository.save(cart);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        return CartMapper.toResponse(getOrCreateCart(securityUtils.getCurrentUser()));
    }

    @Override
    @Transactional
    public CartResponse addItem(Long productId) {
        Cart cart = getOrCreateCart(securityUtils.getCurrentUser());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if (!product.isAvailable()) {
            throw new BusinessValidationException(product.getName() + " is currently unavailable.");
        }

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + 1);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(1);
            cart.getItems().add(item);
        }
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long productId) {
        Cart cart = getOrCreateCart(securityUtils.getCurrentUser());
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return CartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse decreaseItem(Long productId) {
        Cart cart = getOrCreateCart(securityUtils.getCurrentUser());

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not in cart."));

        if (existing.getQuantity() <= 1) {
            cart.getItems().remove(existing);
        } else {
            existing.setQuantity(existing.getQuantity() - 1);
        }
        return CartMapper.toResponse(cartRepository.save(cart));
    }
}