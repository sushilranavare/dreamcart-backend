package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.ChangePasswordRequest;
import com.dreamcart.backend.dto.request.UpdateProfileRequest;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    /*
     * Get the profile of the currently authenticated user.
     * Accessible by any logged-in user (USER or ADMIN).
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return ResponseEntity.ok(
                userService.getProfile(authentication.getName())
        );
    }

    /*
     * Update the profile (name, phone) of the currently authenticated user.
     * Accessible by any logged-in user (USER or ADMIN).
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                userService.updateProfile(authentication.getName(), request)
        );
    }

    /*
     * Change the password of the currently authenticated user.
     * Requires the current password to be provided and correct.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok("Password updated successfully.");
    }
}