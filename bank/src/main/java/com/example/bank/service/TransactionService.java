package com.example.bank.service;

import com.example.bank.dto.TransactionDTO;
import com.example.bank.dto.request.TransactionRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionDTO processTransaction(TransactionRequest request);
    TransactionDTO getTransactionById(Long id);
    List<TransactionDTO> getTransactionsByAccountId(Long accountId);
    List<TransactionDTO> getTransactionsByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end);
}
