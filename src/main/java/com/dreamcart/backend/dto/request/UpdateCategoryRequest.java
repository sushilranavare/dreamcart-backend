package com.dreamcart.backend.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}     
