package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}