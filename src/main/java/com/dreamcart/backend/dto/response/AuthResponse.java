package com.dreamcart.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
 * This DTO is used to return authentication results to the client.
 * It contains the JWT token and a simple status message.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    // JWT token generated after successful authentication
    private String token;

    //Authenticated users role
    private String role;

    // Success message for registration or login
    private String message;


}