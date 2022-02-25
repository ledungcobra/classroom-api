package com.ledungcobra.mail;

public interface EmailService {
    void sendMail(String to, String subject, String buttonTitle, String path, String title, String content);
}
