package com.ledungcobra.user.service;

import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.dto.user.update.UpdateProfileRequest;
import com.ledungcobra.user.entity.AppRole;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.oauth2.CustomOAuth2User;
import com.ledungcobra.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ledungcobra.common.AuditUtils.createAudit;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Autowired
    @Lazy
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User register(RegisterUserDto data, ERoleAccount roleAccount) {

        var user = User.builder()
                .userName(data.getUsername())
                .normalizedUserName(data.getUsername().toUpperCase())
                .firstName(data.getFirstName())
                .middleName(data.getMiddleName())
                .lastName(data.getLastName())
                .normalizedDisplayName(String.format("%s %s %s", data.getFirstName(), data.getMiddleName(), data.getLastName()))
                .email(data.getEmail())
                .normalizedEmail(data.getEmail() != null ? data.getEmail().toUpperCase() : null)
                .personalEmail(data.getEmail())
                .normalizedPersonalEmail(data.getEmail() != null ? data.getEmail().toUpperCase() : null)
                .personalEmailConfirmed((byte) 0)
                .phoneNumber(data.getPhoneNumber())
                .personalPhoneNumber(data.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(data.getPassword()))
                .role(entityManager.getReference(AppRole.class, roleAccount.getValue()))
                .securityStamp("")
                .concurrencyStamp("")
                .twoFactorEnabled((byte) 0)
                .lockoutEnabled((byte) 1)
                .profileImageUrl(data.getImageUrl() != null ? data.getImageUrl() : "")
                .accessFailedCount(0)
                .emailConfirmed((byte) 0)
                .gender(EGender.None.getValue())
                .phoneNumberConfirmed((byte) 1)
                .roleAccount(ERoleAccount.User.getValue())
                .provider(Provider.LOCAL)
                .userStatus(EUserStatus.NotSpecified.getValue())
                .build();

        return userRepository.save(createAudit(user, data.getUsername()));
    }

    @Override
    public User register(RegisterUserDto data) {
        return register(data, ERoleAccount.User);
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Not found user for %s", username)));
    }

    @Override
    public boolean checkExists(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public User processOAuthPostLogin(String email, CustomOAuth2User user) {
        var foundUser = userRepository.findByEmail(email);
        if (foundUser == null) {

            var gender = user.getAttributes().get("gender").toString();
            var firstName = user.getAttributes().get("name").toString();
            var familyName = user.getAttributes().get("family_name").toString();
            var avatarUrl = user.getAttributes().get("picture").toString();

            var registerUser = register(RegisterUserDto.builder()
                    .username(UUID.randomUUID().toString())
                    .email(email)
                    .firstName(firstName)
                    .middleName("")
                    .lastName(familyName)
                    .imageUrl(avatarUrl)
                    .password("")
                    .phoneNumber("")
                    .build());

            registerUser.setEmailConfirmed((byte) 1);
            registerUser.setUserStatus(EUserStatus.Active.getValue());
            registerUser.setProvider(Provider.GOOGLE);
            registerUser.setGender("male".equals(gender) ? EGender.Male.getValue() : EGender.Female.getValue());
            return userRepository.save(registerUser);
        }
        return foundUser;
    }

    @Override
    public User updateProfile(User user, UpdateProfileRequest data) {

        user.setPhoneNumber(StringUtils.hasText(data.getPhoneNumber()) ? data.getPhoneNumber() : user.getPhoneNumber());
        user.setFirstName(StringUtils.hasText(data.getFirstName()) ? data.getFirstName() : user.getFirstName());
        user.setMiddleName(StringUtils.hasText(data.getMiddleName()) ? data.getMiddleName() : user.getMiddleName());
        user.setLastName(StringUtils.hasText(data.getLastName()) ? data.getLastName() : user.getLastName());
        user.setPersonalEmail(StringUtils.hasText(data.getPersonalEmail()) ? data.getPersonalEmail() : user.getPersonalEmail());
        user.setPersonalPhoneNumber(StringUtils.hasText(data.getPersonalPhoneNumber()) ? data.getPersonalPhoneNumber() : user.getPersonalPhoneNumber());
        user.setStudentID(StringUtils.hasText(data.getStudentID()) ? data.getStudentID() : user.getStudentID());
        user.setProfileImageUrl(StringUtils.hasText(data.getProfileImageUrl()) ? data.getProfileImageUrl() : user.getProfileImageUrl());
        return userRepository.save(AuditUtils.updateAudit(user, user.getUserName()));
    }

    @Override
    public void update(User user) {
        userRepository.save(AuditUtils.updateAudit(user, user.getUserName() == null ? "" : user.getUserName()));
    }

    @Override
    public User findByStudentCode(String studentCode) {
        return userRepository.findByStudentID(studentCode);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean validateToken(User user, String token) {
        return jwtUtils.validateToken(token) &&
                Objects.equals(user.getUserName(), jwtUtils.getUserNameFromJwtToken(token));
    }


    @Override
    public boolean confirmEmail(User user, String token) {
        try {
            if (validateToken(user, token)) {
                user.setEmailConfirmed((byte) 1);
                user.setUserStatus(EUserStatus.Active.getValue());
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean resetPassword(User user, String token, String defaultPassword) {
        if (validateToken(user, token)) {
            user.setPasswordHash(passwordEncoder.encode(defaultPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> findByRoleAndUsername(int roleId, String username, Pageable page) {
        return userRepository.findByRoleIdAndUsername(roleId, username);
    }

    @Override
    public User findAdminById(Integer id) {
        return userRepository.findByIdRoleId(id, ERoleAccount.Admin.getValue());
    }

    @Override
    public long countByRole(int roleId) {
        return userRepository.countByRoleId(roleId);
    }

}
