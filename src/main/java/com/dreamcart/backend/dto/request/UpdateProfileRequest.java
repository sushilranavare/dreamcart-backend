package com.dreamcart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
 * This DTO is used to receive profile update data from the client.
 * Email cannot be changed here since it is the user's login identifier.
 */
@Getter
@Setter
public class UpdateProfileRequest {

    // User's first name
    @NotBlank(message = "First name is required")
    private String firstName;

    // User's last name
    @NotBlank(message = "Last name is required")
    private String lastName;

    // User's phone number
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
}