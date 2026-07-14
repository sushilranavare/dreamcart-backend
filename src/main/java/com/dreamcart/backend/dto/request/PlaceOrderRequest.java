package com.dreamcart.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotNull(message = "Shipping address is required")
    private Long addressId;
}