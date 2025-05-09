package com.example.bank.controller;

import com.example.bank.dto.TransferDTO;
import com.example.bank.dto.request.TransferRequest;
import com.example.bank.dto.response.ApiResponse;
import com.example.bank.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransferDTO>> processTransfer(@Valid @RequestBody TransferRequest request) {
        TransferDTO transferDTO = transferService.processTransfer(request);
        return new ResponseEntity<>(ApiResponse.success("Transfer processed successfully", transferDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransferDTO>> getTransferById(@PathVariable Long id) {
        TransferDTO transferDTO = transferService.getTransferById(id);
        return ResponseEntity.ok(ApiResponse.success("Transfer retrieved successfully", transferDTO));
    }

    @GetMapping("/source-account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransferDTO>>> getTransfersBySourceAccountId(@PathVariable Long accountId) {
        List<TransferDTO> transfers = transferService.getTransfersBySourceAccountId(accountId);
        return ResponseEntity.ok(ApiResponse.success("Transfers retrieved successfully", transfers));
    }

    @GetMapping("/destination-account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransferDTO>>> getTransfersByDestinationAccountId(@PathVariable Long accountId) {
        List<TransferDTO> transfers = transferService.getTransfersByDestinationAccountId(accountId);
        return ResponseEntity.ok(ApiResponse.success("Transfers retrieved successfully", transfers));
    }

    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<ApiResponse<List<TransferDTO>>> getTransfersByAccountIdAndDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "true") boolean isSource) {
        List<TransferDTO> transfers = transferService.getTransfersByAccountIdAndDateRange(accountId, start, end, isSource);
        return ResponseEntity.ok(ApiResponse.success("Transfers retrieved successfully", transfers));
    }
}
