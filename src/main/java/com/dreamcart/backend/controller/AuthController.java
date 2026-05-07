/*
 * This controller exposes authentication-related APIs.
 * It provides endpoints for user registration and login.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.LoginRequest;
import com.dreamcart.backend.dto.request.RegisterRequest;
import com.dreamcart.backend.dto.response.AuthResponse;
import com.dreamcart.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
//Registers a new user and returns a JWT token after successful signup.
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
//Authenticates an existing user and returns a JWT token after successful login.
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}