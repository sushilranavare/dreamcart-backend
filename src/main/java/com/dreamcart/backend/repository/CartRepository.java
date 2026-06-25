package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.Cart;
import com.dreamcart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Cart entity.
 * Provides database operations for shopping carts.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find a cart belonging to a specific user
    Optional<Cart> findByUser(User user);
}