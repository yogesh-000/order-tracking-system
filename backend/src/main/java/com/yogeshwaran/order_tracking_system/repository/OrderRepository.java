package com.yogeshwaran.order_tracking_system.repository;

import com.yogeshwaran.order_tracking_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long customerId);
}
