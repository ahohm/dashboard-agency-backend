package com.akkurad.dashboardagencybackend.controller;



import com.akkurad.dashboardagencybackend.dto.Mail;
import com.akkurad.dashboardagencybackend.service.impl.MailServiceImpl;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private MailServiceImpl mailService;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }


    @PostMapping("/sendMail")
    public void sendMail() throws MessagingException, TemplateException, IOException {

        Mail mail = new Mail();

        Map<String , Object> model = new HashMap<>();
        model.put("name", "USER_1");
        model.put("body", "link token here");

        mail.setMailSubject("USER_1");
        mail.setMailFrom("no@one.com");
        mail.setMailTo("ahmed.hmayer@gmail.com");
        mail.setMailContent("mail content .");
        mail.setMailHtmlContent("createAccountVerification.html");
        mail.setModel(model);

        mailService.sendEmail(mail);
    }
}
