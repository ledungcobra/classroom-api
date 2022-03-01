package com.ledungcobra.user.repository;

import com.ledungcobra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByStudentID(String studentID);

    @Query("FROM User u where u.role.id = ?1 and u.userName like ?2")
    List<User> findByRoleIdAndUsername(int roleId, String username);

    @Query("FROM User u where u.id = ?1 and u.role.id = ?2")
    User findByIdRoleId(Integer userId, int role);

    long countByRoleId(int roleId);
}