package com.example.bank.controller;

import com.example.bank.dto.CustomerDTO;
import com.example.bank.dto.request.CustomerRegistrationRequest;
import com.example.bank.dto.response.ApiResponse;
import com.example.bank.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Add this annotation to allow public access to the customer endpoints
@CrossOrigin(origins = "*", maxAge = 3600)
// Update the class declaration to include PreAuthorize annotations
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        CustomerDTO customerDTO = customerService.registerCustomer(request);
        return new ResponseEntity<>(ApiResponse.success("Customer registered successfully", customerDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", customerDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@PathVariable Long id,
                                                                   @Valid @RequestBody CustomerRegistrationRequest request) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", customerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }
}
