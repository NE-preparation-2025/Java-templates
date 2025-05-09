package com.example.bank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotBlank(message = "Transaction type is required")
    private String type; // DEPOSIT, WITHDRAWAL

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
