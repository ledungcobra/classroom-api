package com.ledungcobra.controller;

import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/Email/")
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class EmailController {

    private final UserService userService;
    @Value("${spring.client-url}")
    private String clientURL;

    // TODO testing
    @GetMapping("ConfirmEmail")
    public RedirectView confirmEmail(@RequestParam("token") String token,
                                     @RequestParam("email") String email) {

        User user = userService.findByEmail(email);
        var redirectView = new RedirectView();
        redirectView.setPropagateQueryParams(true);
        if (user == null) {
            redirectView.setUrl(clientURL + "/login?error=true");
            return redirectView;
        }
        var success = userService.confirmEmail(user, token);
        if (success) {
            redirectView.setUrl(clientURL + "/login?email-confirm=true");
        } else {
            redirectView.setUrl(clientURL + "/login?error=true");
        }
        return redirectView;
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
