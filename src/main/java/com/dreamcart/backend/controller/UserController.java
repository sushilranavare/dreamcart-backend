package com.dreamcart.backend.controller;

import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 1. Tells Spring this handles HTTP API requests
@RequestMapping("/api/users") // 2. Maps these endpoints to /api/users
public class UserController {

    // 3. Declare the service variable
    private final UserService userService;

    // 4. Inject the service using a constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}