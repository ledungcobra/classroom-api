package com.ledungcobra.user.repository;

import com.ledungcobra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByStudentID(String studentID);
}