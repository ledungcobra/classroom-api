package com.ledungcobra.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String message, String link, String title, String content) {
        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("link", link);
        context.setVariable("title", title);
        context.setVariable("content", content);
        return templateEngine.process("template/mail-template", context);
    }

}