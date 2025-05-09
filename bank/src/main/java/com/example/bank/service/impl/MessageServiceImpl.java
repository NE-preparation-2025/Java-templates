package com.example.bank.service.impl;

import com.example.bank.dto.MessageDTO;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.model.Customer;
import com.example.bank.model.Message;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.repository.MessageRepository;
import com.example.bank.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public MessageDTO createMessage(Long customerId, String messageContent) {
        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        // Create message
        Message message = new Message();
        message.setCustomer(customer);
        message.setMessage(messageContent);

        // Save message
        Message savedMessage = messageRepository.save(message);

        // Return DTO
        return mapToDTO(savedMessage);
    }

    @Override
    public List<MessageDTO> getMessagesByCustomerId(Long customerId) {
        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return messageRepository.findByCustomerOrderByDateTimeDesc(customer).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO mapToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getCustomer().getId(),
                message.getCustomer().getFirstName() + " " + message.getCustomer().getLastName(),
                message.getMessage(),
                message.getDateTime()
        );
    }
}
