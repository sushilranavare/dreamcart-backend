/*This DTO is used to send product details back to the client.
* It keeps the API response clean and avoids returning the full product entity.*/
package com.dreamcart.backend.dto.response;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private Boolean isActive; // Shows whether the product is active and available in the system.
    private Long categoryId;
    private String categoryName;

}
