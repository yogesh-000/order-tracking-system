package com.yogeshwaran.order_tracking_system.repository;

import com.yogeshwaran.order_tracking_system.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer_Id(Long customerId);
}