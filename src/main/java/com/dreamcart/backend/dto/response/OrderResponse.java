package com.dreamcart.backend.dto.response;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {

    private Long orderId;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String orderStatus;
    private String paymentStatus;
    private String transactionId;
    
}
