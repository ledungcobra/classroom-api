package com.ledungcobra.course.service;

import com.ledungcobra.mail.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles({"default","test"})
@Transactional
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void sendMail() {
        emailService.sendMail("ledungcobra@gmail.com","Hello","Confirm password", "","Title","Content");
    }
}