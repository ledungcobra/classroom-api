package com.ledungcobra.user.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@FlywayTest
@ComponentScan(basePackages = {"com.ledungcobra.user", "com.ledungcobra.course", "com.ledungcobra.common", "com.ledungcobra.configuration.database"})
class UserRepositoryTest {

}