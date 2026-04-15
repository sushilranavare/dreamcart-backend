/* This DTO is used to receive product creation data from the client.
Helps to validate input and avoid exposing product entity directly.
* */
package com.dreamcart.backend.dto.request;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Product name is required") // name required cause it cannot exist without name.
    private String name;
    private String description;

    @NotNull(message = "Price is required") // Price must be greater than 0 for valid product creation
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    private String imageUrl; // optional image URL to display product image on the screen.

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
