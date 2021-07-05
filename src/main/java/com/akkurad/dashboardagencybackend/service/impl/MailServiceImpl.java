package com.akkurad.dashboardagencybackend.service.impl;

import com.akkurad.dashboardagencybackend.dto.Mail;
import com.akkurad.dashboardagencybackend.payload.request.SaveOwnerRequest;
import com.akkurad.dashboardagencybackend.security.jwt.JwtUtils;
import com.akkurad.dashboardagencybackend.service.IMailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import freemarker.template.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration fmConfiguration;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JwtUtils jwtUtils;


//    public void sendEmail(Mail mail) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//        try {
//
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//
//            mimeMessageHelper.setSubject(mail.getMailSubject());
//            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
//            mimeMessageHelper.setTo(mail.getMailTo());
//            mimeMessageHelper.setText(mail.getMailContent());
//
//            mailSender.send(mimeMessageHelper.getMimeMessage());
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }



    public void sendEmail(Mail mail) throws IOException, TemplateException, MessagingException {
//        String validationToken = jwtUtils.generateJwtToken(new SaveOwnerRequest(mail.getMailTo(), mail.getMailSubject()));

        Context ctx = new Context();
        mail.getModel().forEach((s, o) -> ctx.setVariable(s, o));

//        Template template = fmConfiguration.getTemplate("createAccountVerification.html");
        Template template = fmConfiguration.getTemplate((String) ctx.getVariable("template"));
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());

        MimeMessage mimeMessage =mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(mail.getMailFrom());
        mimeMessageHelper.setTo(mail.getMailTo());
        mimeMessageHelper.setSubject(mail.getMailSubject());
        // Build html text with Thymeleaf template
//        Context ctx = new Context();
        // Context of the parameters of the template
//        ctx.setVariable("name", mail.getModel().get("name"));
//        ctx.setVariable("body", "http://localhost:8080/api/owner/validateAccount?validationToken="+validationToken);
        String emailText = templateEngine.process(mail.getMailHtmlContent(), ctx);
        mimeMessageHelper.setText(emailText, true);
//        FileSystemResource logoImage = new FileSystemResource(imagePath);
        // General picture calls this method
//        mimeMessageHelper.addInline("logoImage", logoImage);
        // General file attachment call this method
        //mimeMessageHelper.addAttachment("logoImage", logoImage);
        mailSender.send(mimeMessage);
    }

}

