package com.yogeshwaran.order_tracking_system.util;

import com.yogeshwaran.order_tracking_system.dto.product.ProductResponse;
import com.yogeshwaran.order_tracking_system.entity.Product;

public class ProductMapper {
    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getImageUrl(), p.isAvailable());
    }
}
