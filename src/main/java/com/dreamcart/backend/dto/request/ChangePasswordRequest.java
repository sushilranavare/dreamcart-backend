package com.dreamcart.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/*
 * This DTO is used to receive password change data from the client.
 * The current password is required so a stolen session token alone
 * cannot be used to lock the real owner out of their account.
 */
@Getter
@Setter
public class ChangePasswordRequest {

    // The user's existing password, verified before any change is made
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    // The new password to replace it with
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "New password must be at least 6 characters long")
    private String newPassword;
}