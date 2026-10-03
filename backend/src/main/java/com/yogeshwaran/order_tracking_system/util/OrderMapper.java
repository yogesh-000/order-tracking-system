package com.yogeshwaran.order_tracking_system.util;

import com.yogeshwaran.order_tracking_system.dto.order.OrderItemResponse;
import com.yogeshwaran.order_tracking_system.dto.order.OrderResponse;
import com.yogeshwaran.order_tracking_system.entity.Order;

import java.util.List;

public class OrderMapper {
    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProduct().getName(), i.getQuantity(), i.getPriceAtOrder()))
                .toList();
        return new OrderResponse(order.getId(), order.getStatus().name(), order.getTotalAmount(),
                order.getPlacedAt(), items);
    }
}