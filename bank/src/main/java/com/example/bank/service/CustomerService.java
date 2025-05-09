package com.example.bank.service;

import com.example.bank.dto.CustomerDTO;
import com.example.bank.dto.request.CustomerRegistrationRequest;

import java.util.List;

public interface CustomerService {
    CustomerDTO registerCustomer(CustomerRegistrationRequest request);
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Long id, CustomerRegistrationRequest request);
    void deleteCustomer(Long id);
}
