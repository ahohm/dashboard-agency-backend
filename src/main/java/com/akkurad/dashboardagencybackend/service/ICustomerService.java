package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.model.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    Customer save(Customer customer) throws Exception;
    List<Customer> findAll();
    Optional<Customer> findById(String id);
    Customer findByUsername(String username);
    void deleteById(String id) throws Exception;
    Customer update(Customer customer);
}
