package com.example.bank.service;

public interface EmailService {
    void sendTransactionNotification(Long customerId, String transactionType, String amount, String accountNumber);
}
