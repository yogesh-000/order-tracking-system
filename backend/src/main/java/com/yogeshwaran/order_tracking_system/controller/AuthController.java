package com.yogeshwaran.order_tracking_system.controller;

import com.yogeshwaran.order_tracking_system.dto.auth.LoginRequest;
import com.yogeshwaran.order_tracking_system.dto.auth.LoginResponse;
import com.yogeshwaran.order_tracking_system.dto.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yogeshwaran.order_tracking_system.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Registered successfully. You can now log in.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
