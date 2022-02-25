package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.dto.common.CreateStudentNotificationSingleArgs;
import com.ledungcobra.dto.common.CreateStudentNotificationsArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface NotificationService {
    List<Notification> createStudentNotifications(CreateStudentNotificationsArgs args);

    Notification createStudentNotification(CreateStudentNotificationSingleArgs build);
}
