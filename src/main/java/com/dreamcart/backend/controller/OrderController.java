/*
 * This controller manages all order-related API requests.
 * It allows users to place orders and retrieve their order history.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.entity.Order;
import com.dreamcart.backend.service.OrderService;
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
     */
    @PostMapping
    public Order placeOrder(@RequestParam Long userId) {
        return orderService.placeOrder(userId);
    }

    /**
     * Get all orders for a user.
     */
    @GetMapping
    public List<Order> getOrders(@RequestParam Long userId) {
        return orderService.getOrders(userId);
    }

    /**
     * Get a single order by its ID.
     */
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }
}