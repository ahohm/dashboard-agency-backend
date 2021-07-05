package com.akkurad.dashboardagencybackend.service;

import com.akkurad.dashboardagencybackend.dto.Mail;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IMailService {
    void sendEmail(Mail mail) throws IOException, TemplateException, MessagingException;
}
