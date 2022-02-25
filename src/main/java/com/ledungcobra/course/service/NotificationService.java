package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.dto.common.CreateStudentNotificationSingleArgs;
import com.ledungcobra.dto.common.CreateStudentNotificationsArgs;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface NotificationService {
    List<Notification> createStudentNotifications(CreateStudentNotificationsArgs args);

    Notification createStudentNotification(CreateStudentNotificationSingleArgs build);

    long countByUserId(Integer userId);

    List<Notification> findAllByUserId(Integer id, Pageable pageable);

    List<Notification> findAllByUserId(Integer id);

    Notification findByUserIdAndId(Integer userId, Integer id);

    void save(Notification notification, String userName);

    void updateBatch(List<Notification> notifications);
}
