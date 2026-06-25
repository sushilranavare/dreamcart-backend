package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.Cart;
import com.dreamcart.backend.entity.CartItem;
import com.dreamcart.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CartItem entity.
 * Handles cart item database operations.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Get all items in a cart
    List<CartItem> findByCart(Cart cart);

    // Check if a product already exists in a cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Delete all items from a cart
    void deleteByCart(Cart cart);
}