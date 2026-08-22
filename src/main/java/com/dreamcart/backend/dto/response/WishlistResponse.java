package com.dreamcart.backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class WishlistResponse {
    private Long id; //The wishlist item id
    private Long productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;
    private Integer stockQuantity;
}
