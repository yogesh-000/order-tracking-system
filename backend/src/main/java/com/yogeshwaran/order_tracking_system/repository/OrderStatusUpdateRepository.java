package com.yogeshwaran.order_tracking_system.repository;

import com.yogeshwaran.order_tracking_system.entity.OrderStatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusUpdateRepository extends JpaRepository<OrderStatusUpdate, Long> {
    List<OrderStatusUpdate> findByOrder_IdOrderByChangedAtAsc(Long orderId);
}
