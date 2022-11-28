package com.example.banksystem.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final Logger logger = LogManager.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(
            String userEmail, String fullName){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("banking@mail.com");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject(userEmail);
            mimeMessageHelper.setText(generateMessageContent(fullName),true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            logger.warn("Email sending was interrupted by MimeMeMessageHelper error");
        }
    }

    private String generateMessageContent(String fullName){
        Context ctx = new Context();
        ctx.setVariable("fullName",fullName);
       return templateEngine.process("email/registration", ctx);
    }
}
