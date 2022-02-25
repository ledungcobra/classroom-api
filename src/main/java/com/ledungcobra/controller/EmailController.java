package com.ledungcobra.controller;

import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/Email/")
@RequiredArgsConstructor
public class EmailController {

    private final UserService userService;

    // TODO testing
    @GetMapping("ConfirmEmail")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token,
                                          @RequestParam("email") String email) {

        User user = userService.findByEmail(email);
        if (user == null) {
            return ok("Not found user");
        }
        var success = userService.confirmEmail(user, token);
        return ok(success ? "ConfirmEmail" : "Error");
    }

    // TODO Testing
    @GetMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token,
                                           @RequestParam("email") String email) {
        var user = userService.findByEmail(email);
        if (user == null) {
            return ok("User not found");
        }
        String randomPass = UUID.randomUUID().toString();
        if (randomPass.length() > 8) {
            randomPass = randomPass.substring(0, 8);
        }
        var result = userService.resetPassword(user, token, randomPass);
        return ok(result ? randomPass : "Error");
    }


}
