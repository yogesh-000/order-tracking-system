package com.yogeshwaran.order_tracking_system.repository;

import com.yogeshwaran.order_tracking_system.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByAvailableTrue(Pageable pageable);

}
