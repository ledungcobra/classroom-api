package com.ledungcobra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ClassroomApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassroomApiApplication.class, args);
    }

}
