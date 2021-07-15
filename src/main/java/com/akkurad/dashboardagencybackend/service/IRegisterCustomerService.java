package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.model.Customer;

public interface IRegisterCustomerService {

    Customer save(Customer customerToRegister) throws Exception;
    void remove(Customer customerToRemove) throws Exception;
}
