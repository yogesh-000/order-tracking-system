package com.yogeshwaran.order_tracking_system.service;

import com.yogeshwaran.order_tracking_system.dto.auth.LoginRequest;
import com.yogeshwaran.order_tracking_system.dto.auth.LoginResponse;
import com.yogeshwaran.order_tracking_system.dto.auth.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
