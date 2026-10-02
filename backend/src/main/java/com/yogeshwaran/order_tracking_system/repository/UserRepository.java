package com.yogeshwaran.order_tracking_system.repository;

import com.yogeshwaran.order_tracking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
