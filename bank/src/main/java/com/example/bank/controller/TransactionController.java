package com.example.bank.controller;

import com.example.bank.dto.TransactionDTO;
import com.example.bank.dto.request.TransactionRequest;
import com.example.bank.dto.response.ApiResponse;
import com.example.bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> processTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionDTO transactionDTO = transactionService.processTransaction(request);
        return new ResponseEntity<>(ApiResponse.success("Transaction processed successfully", transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        TransactionDTO transactionDTO = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", transactionDTO));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }

    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByAccountIdAndDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountIdAndDateRange(accountId, start, end);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }
}
