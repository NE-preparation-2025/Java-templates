package com.naome.template.auth.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InitiatePasswordResetDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid.")
        String email
) {
}