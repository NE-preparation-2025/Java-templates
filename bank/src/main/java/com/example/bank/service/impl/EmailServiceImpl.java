package com.example.bank.service.impl;

import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.model.Customer;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final CustomerRepository customerRepository;

    @Override
    @Async
    public void sendTransactionNotification(Long customerId, String transactionType, String amount, String accountNumber) {
        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        // Create email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Transaction Notification");

        String emailContent = String.format(
                "Dear %s,\n\nYour %s of %s on your account %s has been Completed Successfully.\n\nThank you for banking with us.\n\nNational Bank of Rwanda",
                customer.getFirstName() + " " + customer.getLastName(),
                transactionType,
                amount,
                accountNumber
        );

        message.setText(emailContent);

        // Send email
        mailSender.send(message);
    }
}
