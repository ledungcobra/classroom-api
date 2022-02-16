package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface NotificationRepository<T extends Notification> extends JpaRepository<T, Integer> {
}