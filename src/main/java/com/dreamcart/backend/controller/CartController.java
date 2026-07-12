package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.AddToCartRequest;
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
    public List<CartItem> getCart(
            @RequestParam Long userId) {

        return cartService.getCart(userId);
    }

    /**
     * Remove item.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{id}")
    public String removeItem(@PathVariable Long id) {

        cartService.removeItem(id);
        return "Item removed successfully";
    }

    /**
     * Clear cart.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    public String clearCart(
            @RequestParam Long userId) {

        cartService.clearCart(userId);
        return "Cart cleared successfully";
    }
}