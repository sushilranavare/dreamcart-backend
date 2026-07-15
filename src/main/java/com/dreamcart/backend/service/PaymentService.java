/*
 * This service contains the business logic for processing
 * payments for customer orders.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.PaymentRequest;
import com.dreamcart.backend.entity.*;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.OrderRepository;
import com.dreamcart.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    /*
     * Process payment for an order.
     */
    @Transactional
    public Payment makePayment(PaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (paymentRepository.findByOrder(order).isPresent()) {
            throw new RuntimeException("Payment already exists for this order.");
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(order.getTotalAmount());

        /*
         * Simulate successful payment.
         */
        payment.setStatus(PaymentStatus.SUCCESS);

        payment.setTransactionId(
                UUID.randomUUID().toString()
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        /*
         * Update order status after successful payment.
         */
        order.setStatus("CONFIRMED");

        orderRepository.save(order);

        return paymentRepository.save(payment);
    }

    /*
     * Returns payment details by payment ID.
     */
    public Payment getPayment(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));
    }

    /*
     * Returns payment details for an order.
     */
    public Payment getPaymentByOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        return paymentRepository.findByOrder(order)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));
    }
}