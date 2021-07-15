package com.akkurad.dashboardagencybackend.dao;

import com.akkurad.dashboardagencybackend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerDao extends JpaRepository<Customer, String> {
    Customer findByUsername(String username);
}
