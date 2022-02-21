package com.ledungcobra.user.service;

import com.ledungcobra.user.entity.User;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.ledungcobra.user.testdata.UserTest.registerUserDto_Success;
import static com.ledungcobra.user.testdata.UserTest.updateProfile_Success;
import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@FlywayTest
@ComponentScan(basePackages = {
        "com.ledungcobra.user",
        "com.ledungcobra.configuration.beans",
        "com.ledungcobra.course",
        "com.ledungcobra.common",
        "com.ledungcobra.configuration.database"
})
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void findByUsernameShouldSuccess() {
        User user = userService.findByUsername("tanhank2k");
        assertThat(user).isNotNull();
    }

    @Test
    void checkExists() {
        var user = userService.checkExists("tanhank2k");
        assertThat(user).isTrue();
    }

    @Test
    void registerAccountShouldSuccess() {
        userService.register(registerUserDto_Success);
        var foundUser = userService.findByUsername(registerUserDto_Success.getUsername());
        assertThat(foundUser).isNotNull();
    }


    @Test
    void matchPasswordShouldSuccess() {
        var passwordTest = "$2a$10$ZSNWuupCeMjCvESTUlfJzeTMBSWoJT3Z7uSGD4g3F8R1xRdeq/zXe";
        boolean success = passwordEncoder.matches("Abcd1234", passwordTest);
        assertThat(success).isTrue();
    }

    @Test
    void updateUserProfileShouldSuccess() {

        var testUserName = "tanhank2k";
        var user = userService.findByUsername(testUserName);
        userService.updateProfile(user, updateProfile_Success);
        var userUpdated = userService.findByUsername(testUserName);

        assertThat(userUpdated).isNotNull();
        assertThat(userUpdated.getPersonalEmail()).isEqualTo(updateProfile_Success.getPersonalEmail());
        assertThat(userUpdated.getPersonalPhoneNumber()).isEqualTo(updateProfile_Success.getPersonalPhoneNumber());
        assertThat(userUpdated.getFirstName()).isEqualTo(updateProfile_Success.getFirstName());
        assertThat(userUpdated.getMiddleName()).isEqualTo(updateProfile_Success.getMiddleName());
        assertThat(userUpdated.getLastName()).isEqualTo(updateProfile_Success.getLastName());
        assertThat(userUpdated.getStudentID()).isEqualTo(updateProfile_Success.getStudentID());
        assertThat(userUpdated.getProfileImageUrl()).isEqualTo(updateProfile_Success.getProfileImageUrl());
    }

    @Test
    void updateShouldSuccess() {
        var newPassword = "myNewPassword@01";
        var user = userService.findByUsername("tanhank2k");
        var newHashedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(newHashedPassword);
        userService.update(user);
        var foundUser = userService.findByUsername("tanhank2k");
        assertThat(foundUser.getPasswordHash()).isEqualTo(newHashedPassword);
    }

}