package com.example.bank.service.impl;

import com.example.bank.dto.TransactionDTO;
import com.example.bank.dto.request.TransactionRequest;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.exception.InvalidTransactionException;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.EmailService;
import com.example.bank.service.MessageService;
import com.example.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final MessageService messageService;

    @Override
    @Transactional
    public TransactionDTO processTransaction(TransactionRequest request) {
        // Find the account
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));

        // Validate transaction type
        String type = request.getType();
        if (!type.equals("DEPOSIT") && !type.equals("WITHDRAWAL")) {
            throw new InvalidTransactionException("Invalid transaction type. Must be DEPOSIT or WITHDRAWAL");
        }

        // Create new transaction object
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(request.getAmount());
        transaction.setBankingDateTime(LocalDateTime.now());

        // Process transaction logic
        if (type.equals("DEPOSIT")) {
            // Add to balance
            account.setBalance(account.getBalance().add(request.getAmount()));
            transaction.setStatus("COMPLETED");
        } else { // WITHDRAWAL
            // Check for sufficient funds
            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                transaction.setStatus("FAILED");
                transactionRepository.save(transaction);
                throw new InsufficientFundsException("Insufficient funds for withdrawal");
            }
            // Subtract from balance
            account.setBalance(account.getBalance().subtract(request.getAmount()));
            transaction.setStatus("COMPLETED");
        }

        // Save updated account and transaction
        accountRepository.save(account);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Send email notification
        emailService.sendTransactionNotification(
                account.getCustomer().getId(),
                type,
                request.getAmount().toString(),
                account.getAccountNumber()
        );

        // Send internal message
        String messageContent = String.format(
                "Dear %s, your %s of %s on account %s has been completed successfully.",
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),
                type,
                request.getAmount(),
                account.getAccountNumber()
        );
        messageService.createMessage(account.getCustomer().getId(), messageContent);

        // Return DTO
        return mapToDTO(savedTransaction);
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return mapToDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccount(account).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transactionRepository.findByAccountAndBankingDateTimeBetween(account, start, end).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getAccount().getAccountNumber(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBankingDateTime(),
                transaction.getStatus()
        );
    }
}
