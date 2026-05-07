package com.dreamcart.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
 * This DTO is used to receive user registration data from the client.
 * Validation annotations make sure required fields are not empty.
 */
@Getter
@Setter
public class RegisterRequest {

    // User's first name
    @NotBlank(message = "First name is required")
    private String firstName;

    // User's last name
    @NotBlank(message = "Last name is required")
    private String lastName;

    // User's email used for login and identification
    @Email(message = "Email format is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    // Raw password sent by client, later stored in encoded form
    @NotBlank(message = "Password is required")
    private String password;

    // User's phone number
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
}