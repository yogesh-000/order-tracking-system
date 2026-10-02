package com.yogeshwaran.order_tracking_system.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
}
