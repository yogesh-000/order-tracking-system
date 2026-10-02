package com.yogeshwaran.order_tracking_system.dto.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class ProductRequest {
    @NotBlank @Size(min = 2, max = 100)
    private String name;
    @Size(max = 500)
    private String description;
    @NotNull @DecimalMin(value = "0.01")
    private BigDecimal price;
    private String imageUrl;
    private boolean available = true;
}