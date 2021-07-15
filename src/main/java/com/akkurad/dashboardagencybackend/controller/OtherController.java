package com.akkurad.dashboardagencybackend.controller;

import com.akkurad.dashboardagencybackend.dto.SuccessResponse;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.service.IOwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/o")
@Slf4j
public class OtherController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IOwnerService iOwnerService;

    @RequestMapping(value = "/validateAccount/{validationToken}", method = RequestMethod.GET)
    public ResponseEntity<?> validateAccount(@PathVariable("validationToken") String validationToken){
        log.warn(validationToken);
        log.error(jwtUtils.getUserNameFromJwtToken(validationToken));
        try {
            iOwnerService.validate(validationToken);
            return ResponseEntity.ok(new SuccessResponse().builder().message("createAccountVerification.html").build());
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public ResponseEntity<?> activate(){


        try {
            log.debug("dfdfdfd");
            return new ResponseEntity( HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
