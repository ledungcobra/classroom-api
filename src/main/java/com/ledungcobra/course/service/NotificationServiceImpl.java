package com.ledungcobra.course.service;

import com.ledungcobra.common.AuditUtils;
import com.ledungcobra.common.Constants;
import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.course.repository.GradeRepository;
import com.ledungcobra.course.repository.NotificationRepository;
import com.ledungcobra.dto.common.CreateStudentNotificationSingleArgs;
import com.ledungcobra.dto.common.CreateStudentNotificationsArgs;
import com.ledungcobra.dto.common.StudentIdStudentCode;
import com.ledungcobra.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final GradeRepository gradeRepository;

    @Override
    public List<Notification> createStudentNotifications(CreateStudentNotificationsArgs args) {
        var studentCodes = args.getStudents().stream()
                .map(s -> new StudentIdStudentCode(s.getId(), s.getStudentId())).toList();
        return studentCodes.stream()
                .map(code -> userRepository.findByStudentID(code.getStudentCode()))
                .map(u -> Notification.builder()
                        .message(args.getMessage())
                        .courseId(args.getCourseId())
                        .gradeId(gradeRepository
                                .findGradeByStudentId(u.getStudentID(),args.getAssignmentId(),args.getCourseId()).getId())
                        .gradeReviewId(args.getGradeReviewId())
                        .userId(u.getId())
                        .isSeen((byte) 0)
                        .typeNotification(Constants.TYPE_NOTIFICATION_STUDENT)
                        .build())
                .map(n -> notificationRepository.save(AuditUtils.createAudit(n, args.getCurrentUser())))
                .toList();
    }

    @Override
    public Notification createStudentNotification(CreateStudentNotificationSingleArgs args) {
        var u = userRepository.findByStudentID(args.getStudentId());
        var notification = Notification.builder()
                .message(args.getMessage())
                .courseId(args.getCourseId())
                .gradeId(args.getGradeId())
                .gradeReviewId(args.getGradeReviewId())
                .userId(u.getId())
                .typeNotification(Constants.TYPE_NOTIFICATION_STUDENT)
                .build();
        notification.setIsSeen(false);
        return notificationRepository.save(AuditUtils.createAudit(notification, args.getCurrentUser()));
    }
}
