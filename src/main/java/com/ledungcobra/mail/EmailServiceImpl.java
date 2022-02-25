package com.ledungcobra.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application-env.properties")
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.base.url}")
    private String BASE_URL;

    private final MailContentBuilder mailContentBuilder;


    public EmailServiceImpl(JavaMailSender emailSender, MailContentBuilder mailContentBuilder) {
        this.emailSender = emailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    @Override
    public void sendMail(
            String to, String subject, String buttonTitle, String path, String contentStr) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            String content = mailContentBuilder.build(buttonTitle, path, subject, contentStr);
            messageHelper.setText(content, true);
        };
        try {
            emailSender.send(messagePreparator);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}