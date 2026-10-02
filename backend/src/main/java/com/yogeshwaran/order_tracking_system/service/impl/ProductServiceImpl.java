package com.yogeshwaran.order_tracking_system.service.impl;

import com.yogeshwaran.order_tracking_system.dto.product.ProductRequest;
import com.yogeshwaran.order_tracking_system.dto.product.ProductResponse;
import com.yogeshwaran.order_tracking_system.entity.Product;
import com.yogeshwaran.order_tracking_system.exception.ResourceNotFoundException;
import com.yogeshwaran.order_tracking_system.repository.ProductRepository;
import com.yogeshwaran.order_tracking_system.service.ProductService;
import com.yogeshwaran.order_tracking_system.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable).map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAvailableProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByAvailableTrue(pageable).map(ProductMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        apply(product, request);
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        product.setAvailable(false);
        productRepository.save(product);
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setAvailable(request.isAvailable());
    }
}