package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    long countByUserId(Integer userId);

    @Query("FROM Notification n where n.userId = ?1")
    List<Notification> findAllByUserId(Integer id, Pageable pageable);

    List<Notification> findAllByUserId(Integer id);

    Notification findByUserIdAndId(Integer userId, Integer id);
}