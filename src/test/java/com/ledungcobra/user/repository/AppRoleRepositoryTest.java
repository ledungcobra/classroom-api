package com.ledungcobra.user.repository;

import com.ledungcobra.user.entity.AppRole;
import org.assertj.core.api.Assertions;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@FlywayTest
@ComponentScan(basePackages = {"com.ledungcobra.user","com.ledungcobra.course","com.ledungcobra.common","com.ledungcobra.configuration.database"})
class AppRoleRepositoryTest {

    @Autowired
    AppRoleRepository repository;

    @Test
    void findAppRoleByIdShowSuccess() {
        Optional<AppRole> appRoleOptional = repository.findById(1);
        Assertions.assertThat(appRoleOptional).isPresent();
    }

    @Test
    void findAllAppRoleShouldEqualTo2() {
        var roles = repository.findAll();
        Assertions.assertThat(roles.size()).isEqualTo(2);
    }

}