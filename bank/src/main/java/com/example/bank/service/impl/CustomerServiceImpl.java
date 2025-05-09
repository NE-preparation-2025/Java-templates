package com.example.bank.service.impl;

import com.example.bank.dto.CustomerDTO;
import com.example.bank.dto.request.CustomerRegistrationRequest;
import com.example.bank.exception.ResourceAlreadyExistsException;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.model.Customer;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerDTO registerCustomer(CustomerRegistrationRequest request) {
        // Check if email already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // Check if mobile already exists
        if (customerRepository.existsByMobile(request.getMobile())) {
            throw new ResourceAlreadyExistsException("Mobile number already registered");
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setDob(request.getDob());

        // Save customer
        Customer savedCustomer = customerRepository.save(customer);

        // Return DTO
        return mapToDTO(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerRegistrationRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!customer.getEmail().equals(request.getEmail()) &&
                customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // Check if mobile is being changed and if it already exists
        if (!customer.getMobile().equals(request.getMobile()) &&
                customerRepository.existsByMobile(request.getMobile())) {
            throw new ResourceAlreadyExistsException("Mobile number already registered");
        }

        // Update customer
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setMobile(request.getMobile());
        customer.setDob(request.getDob());

        // Save customer
        Customer updatedCustomer = customerRepository.save(customer);

        // Return DTO
        return mapToDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getMobile(),
                customer.getDob()
        );
    }
}
