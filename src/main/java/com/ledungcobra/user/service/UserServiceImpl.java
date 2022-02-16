package com.ledungcobra.user.service;

import com.ledungcobra.common.EGender;
import com.ledungcobra.common.ERoleAccount;
import com.ledungcobra.common.EUserStatus;
import com.ledungcobra.common.Provider;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.user.entity.AppRole;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.oauth2.CustomOAuth2User;
import com.ledungcobra.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.util.UUID;

import static com.ledungcobra.common.AuditUtils.createAudit;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Override
    public User register(RegisterUserDto data) {

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
                .role(entityManager.getReference(AppRole.class, ERoleAccount.User.getValue()))
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

}
