package com.example.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String type; // DEPOSIT, WITHDRAWAL

    private BigDecimal amount;

    private LocalDateTime bankingDateTime;

    private String status; // COMPLETED, FAILED, PENDING

    @PrePersist
    public void prePersist() {
        bankingDateTime = LocalDateTime.now();
    }
}
