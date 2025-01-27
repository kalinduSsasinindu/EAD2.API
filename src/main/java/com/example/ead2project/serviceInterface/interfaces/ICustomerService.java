package com.example.ead2project.serviceInterface.interfaces;

import java.util.List;

import com.example.ead2project.repository.Data.Entities.Customer.Customer;

public interface ICustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(String id);
    void createCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(String id);
    List<Customer> searchCustomers(String query);
    Customer getCustomerByPhone(String phone);
} 