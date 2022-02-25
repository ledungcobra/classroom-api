package com.ledungcobra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Email/")
public class EmailController {

    @GetMapping("ConfirmEmail")
    public ResponseEntity<?> confirmEmail() {
        return null;
    }

    @GetMapping("reset-password")
    public ResponseEntity<?> resetPassword() {
        return null;
    }



}
