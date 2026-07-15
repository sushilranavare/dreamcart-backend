/*
 * This controller handles all payment-related API requests.
 * It allows users to make payments and view payment details.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.PaymentRequest;
import com.dreamcart.backend.entity.Payment;
import com.dreamcart.backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /*
     * Make payment for an order.
     * Accessible by USER.
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Payment makePayment(
            @Valid @RequestBody PaymentRequest request) {

        return paymentService.makePayment(request);
    }

    /*
     * Get payment by payment ID.
     * Accessible by USER.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public Payment getPayment(
            @PathVariable Long id) {

        return paymentService.getPayment(id);
    }

    /*
     * Get payment details using order ID.
     * Accessible by USER.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/order/{orderId}")
    public Payment getPaymentByOrder(
            @PathVariable Long orderId) {

        return paymentService.getPaymentByOrder(orderId);
    }
}