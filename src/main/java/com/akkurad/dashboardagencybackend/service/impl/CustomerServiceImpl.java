package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.dao.ICustomerDao;
import com.akkurad.dashboardagencybackend.dao.IOwnerDao;
import com.akkurad.dashboardagencybackend.dto.Mail;
import com.akkurad.dashboardagencybackend.model.Customer;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.service.ICustomerService;
import com.akkurad.dashboardagencybackend.service.IRegisterCustomerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerDao iCustomerDao;
    @Autowired
    private IRegisterCustomerService registerCustomerService;
    @Autowired
    private RandomStringUtils randomStringUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailServiceImpl mailService;
    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    @Override
    public Customer save(Customer customer) throws Exception {
        customer.setId(ObjectId.get().toString());

        Customer customer1 = iCustomerDao.findByUsername(customer.getUsername());
        if ( customer1 != null){
            throw new RuntimeException("Customer already exist");
        }

//        String validationToken = jwtUtils.generateJwtToken(new SaveCustomerRequest(customer.getEmail(), customer.getUsername()));

        Map<String , Object> model = new HashMap<>();
        model.put("name", customer.getUsername());
        model.put("body", "http://localhost:8080/api/o/validateAccount/"/*+validationToken*/);
        model.put("template", "createAccountVerification.html");

        Mail mail = new Mail();
        mail.setMailTo(customer.getEmail());
        mail.setMailFrom("no@reply.com");
        mail.setMailSubject(customer.getUsername());
//        mail.setMailContent("Hi Mr. "+customer.getUsername()+"\n\nThanks for joining us,\n this is your default password : sdsds\nPlease verify your account from the link down below so you can continue with your application. \nwww.smartlock.com");
        mail.setMailHtmlContent("createAccountVerification.html");
        mail.setModel(model);

        mailService.sendEmail(mail);

        return registerCustomerService.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return iCustomerDao.findAll();
    }

    @Override
    public Optional<Customer> findById(String id) {
        return iCustomerDao.findById(id);
    }

    @Override
    public Customer findByUsername(String username) {
        return iCustomerDao.findByUsername(username);
    }

    @Override
    public void deleteById(String id) throws Exception {
        //registerCustomerServiceImpl.remove(this.findById(id).get());
        iCustomerDao.deleteById(id);
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }
}
