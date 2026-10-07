package com.ecommerce.controller;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.UserRegistration;
import com.ecommerce.entity.User;
import com.ecommerce.security.JwtUtil;
import com.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and registration")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new customer account")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistration request) {
        User user = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and receive access token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.authenticateUser(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail(), user.getUserId());
        
        AuthResponse response = AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400)
                .user(user)
                .build();
        
        return ResponseEntity.ok(response);
    }
}

// Made with Bob
