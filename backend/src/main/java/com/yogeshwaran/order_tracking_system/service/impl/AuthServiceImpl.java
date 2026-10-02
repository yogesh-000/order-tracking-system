package com.yogeshwaran.order_tracking_system.service.impl;

import com.yogeshwaran.order_tracking_system.dto.auth.LoginRequest;
import com.yogeshwaran.order_tracking_system.dto.auth.LoginResponse;
import com.yogeshwaran.order_tracking_system.dto.auth.RegisterRequest;
import com.yogeshwaran.order_tracking_system.entity.Role;
import com.yogeshwaran.order_tracking_system.security.CustomUserDetailsService;
import com.yogeshwaran.order_tracking_system.entity.User;
import com.yogeshwaran.order_tracking_system.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.yogeshwaran.order_tracking_system.repository.UserRepository;
import com.yogeshwaran.order_tracking_system.security.JwtService;
import com.yogeshwaran.order_tracking_system.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already taken.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication."));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, user.getUsername(), user.getRole().name());
    }
}
