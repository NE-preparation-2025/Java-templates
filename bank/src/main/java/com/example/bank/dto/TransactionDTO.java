package com.example.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private Long accountId;
    private String accountNumber;
    private String type;
    private BigDecimal amount;
    private LocalDateTime bankingDateTime;
    private String status;
}
