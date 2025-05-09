package com.example.bank.controller;

import com.example.bank.dto.MessageDTO;
import com.example.bank.dto.response.ApiResponse;
import com.example.bank.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getMessagesByCustomerId(@PathVariable Long customerId) {
        List<MessageDTO> messages = messageService.getMessagesByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Messages retrieved successfully", messages));
    }
}
