package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.TeacherNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherNotificationRepository extends JpaRepository<TeacherNotification, Integer> {
}