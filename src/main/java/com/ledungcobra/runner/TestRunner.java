package com.ledungcobra.runner;

import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PropertySource(value = "classpath:application-env.properties")
public class TestRunner implements CommandLineRunner {

    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void run(String... args) throws Exception {
        log.error("Email is {}", email);
        AppUserDetails user = (AppUserDetails) userDetailsService.loadUserByUsername("tanhank2k");
        String token = jwtUtils.generateToken(user);
        log.info(String.format("JWT TOKEN %s", token));
    }
}
