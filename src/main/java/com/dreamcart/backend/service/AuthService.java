/*
 * This service contains the authentication business logic.
 * It handles user registration, login, password encoding,
 * role assignment, and token generation.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.LoginRequest;
import com.dreamcart.backend.dto.request.RegisterRequest;
import com.dreamcart.backend.dto.response.AuthResponse;
import com.dreamcart.backend.entity.Role;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.repository.RoleRepository;
import com.dreamcart.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
//Registers a new user after checking whether the email is already used.
//Password is stored in encrypted form, and the default USER role is assigned.
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsActive(true);
        user.setRole(userRole);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, "User registered successfully");
    }

    //Registers a new user after checking whether the email is already used.
    // Password is stored in encrypted form, and the default USER role is assigned.
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, "Login successful");
    }
}