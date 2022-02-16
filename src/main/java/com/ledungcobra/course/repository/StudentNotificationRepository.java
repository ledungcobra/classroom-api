package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.StudentNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentNotificationRepository extends JpaRepository<StudentNotification, Integer> {
}