package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.model.Owner;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IOwnerService {
    Owner save(Owner owner) throws Exception;
    List<Owner> findAll();
    Optional<Owner> findById(String id);
    Owner findByUsername(String username);
    void deleteById(String id) throws Exception;
    Owner update(Owner owner);
    void validate(String validationToken) throws TemplateException, MessagingException, IOException;
}
