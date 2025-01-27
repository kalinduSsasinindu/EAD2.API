package com.example.ead2project.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ead2project.repository.Data.Entities.Customer.Customer;
import com.example.ead2project.serviceInterface.interfaces.ICustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    
    private final ICustomerService customerService;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Customer customer) {
        customerService.createCustomer(customer);
        return ResponseEntity.ok(new IdResponse(customer.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomer(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(customerService.searchCustomers(query));
    }
}

@Data
class IdResponse {
    private final String id;
} 