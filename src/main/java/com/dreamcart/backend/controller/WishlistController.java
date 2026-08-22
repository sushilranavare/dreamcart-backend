package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.WishlistRequest;
import com.dreamcart.backend.dto.response.WishlistResponse;
import com.dreamcart.backend.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/add")
    public List<WishlistResponse> addToWishlist(
            Authentication authentication,
            @Valid @RequestBody WishlistRequest request) {
        return wishlistService.addToWishlist(authentication.getName(), request);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<WishlistResponse> getWishlist(Authentication authentication) {
        return wishlistService.getWishlist(authentication.getName());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{productId}")
    public List<WishlistResponse> removeFromWishlist(
            Authentication authentication,
            @PathVariable Long productId) {
        return wishlistService.removeFromWishlist(authentication.getName(), productId);
    }
}