/*This DTO is used when the client sends data to cerate a new category.
* Used instead of entity so that only required input fields are accepted */
package com.dreamcart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required") // category name is mandatory because every category must have valid name
    private String name;
    private String description;
}
