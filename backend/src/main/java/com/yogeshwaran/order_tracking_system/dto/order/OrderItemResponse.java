package com.yogeshwaran.order_tracking_system.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter @AllArgsConstructor
public class OrderItemResponse {
    private String productName;
    private int quantity;
    private BigDecimal priceAtOrder;
}
