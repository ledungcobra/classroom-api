package com.ledungcobra.common;

import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.exception.AuthenticationException;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    @SneakyThrows
    public static AppUserDetails appUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof AppUserDetails)) {
            throw new AuthenticationException("Principal not valid");
        }
        return (AppUserDetails) principal;
    }
}
