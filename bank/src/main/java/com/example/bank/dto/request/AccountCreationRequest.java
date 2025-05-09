package com.example.bank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreationRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Initial deposit amount is required")
    @Positive(message = "Initial deposit must be positive")
    private BigDecimal initialDeposit;
}
