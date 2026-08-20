package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.AddToCartRequest;
import com.dreamcart.backend.dto.request.UpdateCartItemRequest;
import com.dreamcart.backend.dto.response.CartItemResponse;
import com.dreamcart.backend.dto.response.CartResponse;
import com.dreamcart.backend.entity.CartItem;
import com.dreamcart.backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add product to cart.
     * Accessible by ADMIN
     */
    @PreAuthorize("hasRole('USER')")

    @PostMapping("/add")
    public CartItem addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request) {

        return cartService.addToCart(authentication.getName(), request);
    }

    /**
     * View cart.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public CartResponse getCart(Authentication authentication){
        return (CartResponse) cartService.getCart(authentication.getName());
    }

    // Update cart item quantity, Accessible by USER.
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{id}")
    public CartResponse updateQuantity(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateQuantity(authentication.getName(), id, request);
    }
    /**
     * Remove item.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{id}")
    public CartResponse removeItem( Authentication authentication, @PathVariable Long id) {

        return cartService.removeItem(authentication.getName(), id);
    }

    /**
     * Clear cart.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    public CartResponse clearCart(Authentication authentication) {

        return cartService.clearCart(authentication.getName());
    }
}