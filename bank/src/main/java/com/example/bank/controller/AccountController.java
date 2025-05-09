package com.example.bank.controller;

import com.example.bank.dto.AccountDTO;
import com.example.bank.dto.request.AccountCreationRequest;
import com.example.bank.dto.response.ApiResponse;
import com.example.bank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDTO>> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        AccountDTO accountDTO = accountService.createAccount(request);
        return new ResponseEntity<>(ApiResponse.success("Account created successfully", accountDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountById(@PathVariable Long id) {
        AccountDTO accountDTO = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", accountDTO));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountByNumber(@PathVariable String accountNumber) {
        AccountDTO accountDTO = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", accountDTO));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getAccountsByCustomerId(@PathVariable Long customerId) {
        List<AccountDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully", null));
    }
}
