package com.ledungcobra.common;

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

}
