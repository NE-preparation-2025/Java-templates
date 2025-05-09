package com.example.bank.service;

import com.example.bank.dto.AccountDTO;
import com.example.bank.dto.request.AccountCreationRequest;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountCreationRequest request);
    AccountDTO getAccountById(Long id);
    AccountDTO getAccountByNumber(String accountNumber);
    List<AccountDTO> getAccountsByCustomerId(Long customerId);
    void deleteAccount(Long id);
}
