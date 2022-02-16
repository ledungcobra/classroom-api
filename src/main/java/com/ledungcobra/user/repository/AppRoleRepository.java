package com.ledungcobra.user.repository;

import com.ledungcobra.user.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Integer> {
}