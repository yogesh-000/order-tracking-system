package com.yogeshwaran.order_tracking_system.service;

import com.yogeshwaran.order_tracking_system.dto.product.ProductRequest;
import com.yogeshwaran.order_tracking_system.dto.product.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductResponse> getAllProducts(int page, int size);
    Page<ProductResponse> getAvailableProducts(int page, int size);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deactivateProduct(Long id);
}
