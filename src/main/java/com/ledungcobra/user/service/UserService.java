package com.ledungcobra.user.service;

import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.oauth2.CustomOAuth2User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    User register(RegisterUserDto registerDto);

    User findByUsername(String username) throws UsernameNotFoundException;

    boolean checkExists(String username);

    User processOAuthPostLogin(String email, CustomOAuth2User user);

}
