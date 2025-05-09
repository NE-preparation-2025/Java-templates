package com.example.bank.service;

import com.example.bank.dto.TransferDTO;
import com.example.bank.dto.request.TransferRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferService {
    TransferDTO processTransfer(TransferRequest request);
    TransferDTO getTransferById(Long id);
    List<TransferDTO> getTransfersBySourceAccountId(Long accountId);
    List<TransferDTO> getTransfersByDestinationAccountId(Long accountId);
    List<TransferDTO> getTransfersByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end, boolean isSource);
}
