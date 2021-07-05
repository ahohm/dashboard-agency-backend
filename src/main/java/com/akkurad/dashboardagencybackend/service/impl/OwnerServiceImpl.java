package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.dao.IOwnerDao;
import com.akkurad.dashboardagencybackend.dto.Mail;
import com.akkurad.dashboardagencybackend.model.Owner;
import com.akkurad.dashboardagencybackend.payload.request.SaveOwnerRequest;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.service.IOwnerService;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class OwnerServiceImpl implements IOwnerService {

    @Autowired
    private IOwnerDao iOwnerDao;
    @Autowired
    private RegisterOwnerServiceImpl registerOwnerServiceImpl;
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
    public Owner save(Owner owner) throws Exception {
        owner.setId(ObjectId.get().toString());

        Owner owner1 = iOwnerDao.findByUsername(owner.getUsername());
        if ( owner1 != null){
            throw new RuntimeException("Owner already exist");
        }

        String validationToken = jwtUtils.generateJwtToken(new SaveOwnerRequest(owner.getEmail(), owner.getUsername()));

        Map<String , Object> model = new HashMap<>();
        model.put("name", owner.getUsername());
        model.put("body", "http://localhost:8080/api/auth/validateAccount?validationToken="+validationToken);
        model.put("template", "createAccountVerification.html");

        Mail mail = new Mail();
        mail.setMailTo(owner.getEmail());
        mail.setMailFrom("no@reply.com");
        mail.setMailSubject(owner.getUsername());
//        mail.setMailContent("Hi Mr. "+owner.getUsername()+"\n\nThanks for joining us,\n this is your default password : sdsds\nPlease verify your account from the link down below so you can continue with your application. \nwww.smartlock.com");
        mail.setMailHtmlContent("createAccountVerification.html");
        mail.setModel(model);

        mailService.sendEmail(mail);

        return registerOwnerServiceImpl.save(owner);
    }

    @Override
    public List<Owner> findAll() {
        return iOwnerDao.findAll();
    }

    @Override
    public Optional<Owner> findById(String id) {
        return iOwnerDao.findById(id);
    }

    @Override
    public Owner findByUsername(String username) {
        return iOwnerDao.findByUsername(username);
    }

    @Override
    public void deleteById(String id) throws Exception {
        registerOwnerServiceImpl.remove(this.findById(id).get());
        iOwnerDao.deleteById(id);
    }

    @Override
    public Owner update(Owner owner) {
        return iOwnerDao.saveAndFlush(owner);
    }

    @SneakyThrows
    @Override
    public void validate(String validationToken) throws TemplateException, MessagingException, IOException {
        Owner owner = iOwnerDao.findByUsername(jwtUtils.getUserNameFromJwtToken(validationToken));
        String password = randomStringUtils.randomAscii(8);
        owner.setPassword(passwordEncoder.encode(password));
        update(owner);

        log.debug(password);
        Map<String , Object> model = new HashMap<>();
        model.put("password", password);
        model.put("body", "http://localhost:8080/api/owner/activate");
        model.put("template", "passwordRequest.html");

        Mail mail = new Mail();
        mail.setMailTo(owner.getEmail());
        mail.setMailFrom("no@reply.com");
        mail.setMailSubject(owner.getUsername());
//        mail.setMailContent("Hi Mr. "+owner.getUsername()+"\n\nThanks for joining us,\n this is your default password : sdsds\nPlease verify your account from the link down below so you can continue with your application. \nwww.smartlock.com");
        mail.setMailHtmlContent("passwordRequest.html");
        mail.setModel(model);

        mailService.sendEmail(mail);
    }
}
