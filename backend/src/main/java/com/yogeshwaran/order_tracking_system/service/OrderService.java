package com.yogeshwaran.order_tracking_system.service;

import com.yogeshwaran.order_tracking_system.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder();
    List<OrderResponse> getMyOrders();
    List<OrderResponse> getAllOrders(String status);
    OrderResponse updateStatus(Long orderId, String newStatus);

    OrderResponse getOrderById(Long orderId);
    OrderResponse cancelMyOrder(Long orderId);

}