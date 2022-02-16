package com.ledungcobra.course.repository;

import com.ledungcobra.user.entity.ClassRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<ClassRole, Integer> {
}