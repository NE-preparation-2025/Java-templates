package com.example.bank.service;

import com.example.bank.dto.MessageDTO;

import java.util.List;

public interface MessageService {
    MessageDTO createMessage(Long customerId, String message);
    List<MessageDTO> getMessagesByCustomerId(Long customerId);
}
