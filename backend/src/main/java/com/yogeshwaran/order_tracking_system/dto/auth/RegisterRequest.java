package com.yogeshwaran.order_tracking_system.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    @NotBlank @Size(min = 4, max = 30)
    private String username;
    @NotBlank @Size(min = 6)
    private String password;
}