package com.dreamcart.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
 * This DTO is used to receive login credentials from the client.
 */
@Getter
@Setter
public class LoginRequest {

    // Email entered by the user
    @Email(message = "Email format is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    // Password entered by the user
    @NotBlank(message = "Password is required")
    private String password;
}