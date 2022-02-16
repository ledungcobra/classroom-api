package com.ledungcobra.controller;

import com.ledungcobra.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        // :TODO LOGIN ADMIN
        return ok(CommonResponse.builder()
                .content("")
                .build());
    }



}
