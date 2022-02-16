package com.ledungcobra.user.service;

import com.ledungcobra.user.entity.User;
import org.assertj.core.api.Assertions;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.ledungcobra.user.testdata.UserTest.registerUserDto_Success;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@FlywayTest
@ComponentScan(basePackages = {"com.ledungcobra.user",
        "com.ledungcobra.configuration.beans",
        "com.ledungcobra.course", "com.ledungcobra.common", "com.ledungcobra.configuration.database"})
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void findByUsername() {
        User user = userService.findByUsername("tanhank2k");
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    void checkExists() {
        var user = userService.checkExists("tanhank2k");
        Assertions.assertThat(user).isTrue();
    }

    @Test
    void registerAccountShouldSuccess() {
        userService.register(registerUserDto_Success);
        var foundUser = userService.findByUsername(registerUserDto_Success.getUsername());
        Assertions.assertThat(foundUser).isNotNull();
    }


    @Test
    void testMatchPasswordShouldSuccess() {
        var passwordTest = "$2a$10$ZSNWuupCeMjCvESTUlfJzeTMBSWoJT3Z7uSGD4g3F8R1xRdeq/zXe";
        boolean success = passwordEncoder.matches("Abcd1234", passwordTest);
        Assertions.assertThat(success).isTrue();
    }
}