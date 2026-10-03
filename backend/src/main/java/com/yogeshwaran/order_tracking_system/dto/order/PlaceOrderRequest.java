package com.yogeshwaran.order_tracking_system.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class PlaceOrderRequest {
    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}
