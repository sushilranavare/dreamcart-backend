/*
 * This controller exposes REST APIs for wishlist management.
 * Only authenticated users with USER role can manage their wishlist.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.WishlistRequest;
import com.dreamcart.backend.entity.Wishlist;
import com.dreamcart.backend.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    /*
     * Adds a product to the authenticated user's wishlist.
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Wishlist> addToWishlist(
            Authentication authentication,
            @Valid @RequestBody WishlistRequest request) {

        return ResponseEntity.ok(
                wishlistService.addToWishlist(authentication.getName(), request)
        );
    }

    /*
     * Returns all wishlist items of the authenticated user.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<Wishlist>> getWishlist(
            Authentication authentication) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(authentication.getName())
        );
    }

    /*
     * Removes a product from the authenticated user's wishlist.
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWishlist(
            Authentication authentication,
            @PathVariable Long productId) {

        wishlistService.removeFromWishlist(
                authentication.getName(),
                productId
        );

        return ResponseEntity.ok("Product removed from wishlist successfully.");
    }
}