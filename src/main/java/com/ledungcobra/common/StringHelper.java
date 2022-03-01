package com.ledungcobra.common;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.google.common.hash.Hashing.sha256;

public class StringHelper {

    public static String generateHashString(String text) {
        return sha256().hashString(text, StandardCharsets.UTF_8).toString();
    }

    public static boolean check(String token, String code) {
        return Objects.equals(generateHashString(code), token);
    }

    public static String getConfirmationLink(String token, String email, String serverPort) {
        final String SERVER_URL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return String.format("%s/Email/ConfirmEmail?token=%s&email=%s", SERVER_URL, token, email);
    }
}
