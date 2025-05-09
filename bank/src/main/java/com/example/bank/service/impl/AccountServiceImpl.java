package com.example.bank.service.impl;

import com.example.bank.dto.AccountDTO;
import com.example.bank.dto.request.AccountCreationRequest;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public AccountDTO createAccount(AccountCreationRequest request) {
        // Find customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        // Generate account number
        String accountNumber = generateAccountNumber();

        // Create account
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCustomer(customer);
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialDeposit());

        // Save account
        Account savedAccount = accountRepository.save(account);

        // Create initial deposit transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(savedAccount);
        transaction.setType("DEPOSIT");
        transaction.setAmount(request.getInitialDeposit());
        transaction.setStatus("COMPLETED");

        transactionRepository.save(transaction);

        // Return DTO
        return mapToDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return mapToDTO(account);
    }

    @Override
    public AccountDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        return mapToDTO(account);
    }

    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return accountRepository.findByCustomer(customer).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Generate a 10-digit account number
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private AccountDTO mapToDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getCustomer().getId(),
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),
                account.getBalance(),
                account.getAccountType(),
                account.getCreationDate()
        );
    }
}
