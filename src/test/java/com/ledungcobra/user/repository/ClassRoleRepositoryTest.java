package com.ledungcobra.user.repository;

import org.assertj.core.api.Assertions;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@FlywayTest
@ComponentScan(basePackages = {"com.ledungcobra.user", "com.ledungcobra.course", "com.ledungcobra.common", "com.ledungcobra.configuration.database"})
class ClassRoleRepositoryTest {

    @Autowired
    ClassRoleRepository classRoleRepository;

    @Test
    void findOneShouldSuccess() {
        var classRole = classRoleRepository.findById(1);
        Assertions.assertThat(classRole).isPresent();
    }

    @Test
    void findAllShouldHaveSize2() {
        var roles = classRoleRepository.findAll();
        Assertions.assertThat(roles.size()).isEqualTo(2);
    }

}