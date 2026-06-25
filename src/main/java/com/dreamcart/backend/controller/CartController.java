package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.AddToCartRequest;
import com.dreamcart.backend.entity.CartItem;
import com.dreamcart.backend.service.CartService;
import jakarta.validation.Valid;
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
     */
    @PostMapping("/add")
    public CartItem addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody AddToCartRequest request) {

        return cartService.addToCart(userId, request);
    }

    /**
     * View cart.
     */
    @GetMapping
    public List<CartItem> getCart(
            @RequestParam Long userId) {

        return cartService.getCart(userId);
    }

    /**
     * Remove item.
     */
    @DeleteMapping("/items/{id}")
    public String removeItem(@PathVariable Long id) {

        cartService.removeItem(id);
        return "Item removed successfully";
    }

    /**
     * Clear cart.
     */
    @DeleteMapping("/clear")
    public String clearCart(
            @RequestParam Long userId) {

        cartService.clearCart(userId);
        return "Cart cleared successfully";
    }
}