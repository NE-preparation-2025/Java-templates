package com.example.bank.service.impl;

import com.example.bank.dto.TransferDTO;
import com.example.bank.dto.request.TransferRequest;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.model.Account;
import com.example.bank.model.Transfer;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransferRepository;
import com.example.bank.service.EmailService;
import com.example.bank.service.MessageService;
import com.example.bank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final MessageService messageService;

    @Override
    @Transactional
    public TransferDTO processTransfer(TransferRequest request) {
        // Find source account
        Account sourceAccount = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + request.getSourceAccountId()));

        // Find destination account
        Account destinationAccount = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found with id: " + request.getDestinationAccountId()));

        // Check if sufficient funds
        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }

        // Create transfer
        Transfer transfer = new Transfer();
        transfer.setSourceAccount(sourceAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setAmount(request.getAmount());
        transfer.setStatus("COMPLETED");

        // Update account balances
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        // Save accounts and transfer
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        Transfer savedTransfer = transferRepository.save(transfer);

        // Send email notifications
        emailService.sendTransactionNotification(
                sourceAccount.getCustomer().getId(),
                "TRANSFER",
                request.getAmount().toString(),
                sourceAccount.getAccountNumber()
        );

        emailService.sendTransactionNotification(
                destinationAccount.getCustomer().getId(),
                "RECEIVED",
                request.getAmount().toString(),
                destinationAccount.getAccountNumber()
        );

        // Create messages
        String sourceMessage = String.format(
                "Dear %s, Your TRANSFER of %s from your account %s to account %s has been Completed Successfully.",
                sourceAccount.getCustomer().getFirstName() + " " + sourceAccount.getCustomer().getLastName(),
                request.getAmount(),
                sourceAccount.getAccountNumber(),
                destinationAccount.getAccountNumber()
        );
        messageService.createMessage(sourceAccount.getCustomer().getId(), sourceMessage);

        String destinationMessage = String.format(
                "Dear %s, You have RECEIVED %s in your account %s from account %s.",
                destinationAccount.getCustomer().getFirstName() + " " + destinationAccount.getCustomer().getLastName(),
                request.getAmount(),
                destinationAccount.getAccountNumber(),
                sourceAccount.getAccountNumber()
        );
        messageService.createMessage(destinationAccount.getCustomer().getId(), destinationMessage);

        // Return DTO
        return mapToDTO(savedTransfer);
    }

    @Override
    public TransferDTO getTransferById(Long id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + id));
        return mapToDTO(transfer);
    }

    @Override
    public List<TransferDTO> getTransfersBySourceAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transferRepository.findBySourceAccount(account).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferDTO> getTransfersByDestinationAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return transferRepository.findByDestinationAccount(account).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferDTO> getTransfersByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end, boolean isSource) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        if (isSource) {
            return transferRepository.findBySourceAccountAndBankingDateTimeBetween(account, start, end).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } else {
            return transferRepository.findByDestinationAccountAndBankingDateTimeBetween(account, start, end).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
    }

    private TransferDTO mapToDTO(Transfer transfer) {
        return new TransferDTO(
                transfer.getId(),
                transfer.getSourceAccount().getId(),
                transfer.getSourceAccount().getAccountNumber(),
                transfer.getDestinationAccount().getId(),
                transfer.getDestinationAccount().getAccountNumber(),
                transfer.getAmount(),
                transfer.getBankingDateTime(),
                transfer.getStatus()
        );
    }
}
