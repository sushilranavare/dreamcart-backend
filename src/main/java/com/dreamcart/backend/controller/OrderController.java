/*
 * This controller manages all order-related API requests.
 * It allows users to place orders and retrieve their order history.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.entity.Order;
import com.dreamcart.backend.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Place an order using all items currently in the user's cart.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Order placeOrder(Authentication authentication) {
        return orderService.placeOrder(authentication.getName());


    }

    /**
     * Get all orders for a user.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<Order> getOrders(Authentication authentication) {
        return orderService.getOrders(authentication.getName());
    }

    /**
     * Get a single order by its ID.
     * Accessible by USER
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public Order getOrder(Authentication authentication) {
        return orderService.placeOrder(authentication.getName());
    }
}
