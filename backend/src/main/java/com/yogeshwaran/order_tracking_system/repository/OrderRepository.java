package com.yogeshwaran.order_tracking_system.repository;

import com.yogeshwaran.order_tracking_system.entity.Order;
import com.yogeshwaran.order_tracking_system.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long customerId);
    List<Order> findByCustomer_IdOrderByPlacedAtDesc(Long customerId);
    List<Order> findByStatusOrderByPlacedAtDesc(OrderStatus status);
    List<Order> findAllByOrderByPlacedAtDesc();
}
