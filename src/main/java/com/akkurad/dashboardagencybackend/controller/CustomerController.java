package com.akkurad.dashboardagencybackend.controller;

import com.akkurad.dashboardagencybackend.model.Customer;
import com.akkurad.dashboardagencybackend.model.Owner;
import com.akkurad.dashboardagencybackend.payload.request.SaveCustomerRequest;
import com.akkurad.dashboardagencybackend.payload.request.SaveOwnerRequest;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.service.ICustomerService;
import com.akkurad.dashboardagencybackend.service.IOwnerService;
import com.akkurad.dashboardagencybackend.service.IRegisterCustomerService;
import com.akkurad.dashboardagencybackend.service.impl.RegisterOwnerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/customer")
@AllArgsConstructor
@Slf4j
public class CustomerController {
    private ICustomerService iCustomerService;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@Valid @RequestBody SaveCustomerRequest cutomerDto) throws Exception {
        Customer customer = modelMapper.map(cutomerDto, Customer.class);
        log.debug(String.valueOf(customer));
        try {

            return new ResponseEntity(iCustomerService.save(customer), HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
