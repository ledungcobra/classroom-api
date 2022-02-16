package com.ledungcobra.configuration.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

import static com.ledungcobra.common.Constants.SEED;
import static com.ledungcobra.common.Constants.STRENGTH;

@Configuration
public class BeanDeclare {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH, new SecureRandom(SEED.getBytes()));
    }
}
