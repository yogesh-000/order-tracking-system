package com.yogeshwaran.order_tracking_system.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime placedAt;
    private List<OrderItemResponse> items;
}
