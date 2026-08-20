package com.dreamcart.backend.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.aspectj.bridge.IMessage;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Quantity is required!")
    @Min(value=1, message="Quantity must be atleast 1")
    private Integer quantity;


}
