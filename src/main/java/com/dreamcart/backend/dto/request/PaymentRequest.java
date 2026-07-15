package com.dreamcart.backend.dto.request;

import com.dreamcart.backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Order ID is required.")
    private Long orderId;

    @NotNull(message = "Payment method is required.")
    private PaymentMethod paymentMethod;
}
