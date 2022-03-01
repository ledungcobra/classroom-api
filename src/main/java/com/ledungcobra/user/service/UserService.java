package com.ledungcobra.user.service;

import com.ledungcobra.common.ERoleAccount;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.dto.user.update.UpdateProfileRequest;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.oauth2.CustomOAuth2User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    User register(RegisterUserDto registerDto, ERoleAccount roleAccount);

    User register(RegisterUserDto registerDto);

    User findByUsername(String username) throws UsernameNotFoundException;

    boolean checkExists(String username);

    User processOAuthPostLogin(String email, CustomOAuth2User user);

    User updateProfile(User user, UpdateProfileRequest data);

    void update(User user);

    User findByStudentCode(String studentCode);

    User findByEmail(String email);

    boolean confirmEmail(User user, String token);

    boolean resetPassword(User user, String token, String defaultPassword);

    List<User> findByRoleAndUsername(int roleId, String username, Pageable page);

    User findAdminById(Integer id);

    long countByRole(int roleId);
}
