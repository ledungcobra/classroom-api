package com.ledungcobra.course.service;

import com.ledungcobra.common.AuditUtils;
import com.ledungcobra.common.ERole;
import com.ledungcobra.common.ETypeNotification;
import com.ledungcobra.course.entity.GradeReview;
import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.course.entity.Student;
import com.ledungcobra.course.repository.CourseUserRepository;
import com.ledungcobra.course.repository.GradeRepository;
import com.ledungcobra.course.repository.NotificationRepository;
import com.ledungcobra.dto.common.CreateStudentNotificationSingleArgs;
import com.ledungcobra.dto.common.CreateStudentNotificationsArgs;
import com.ledungcobra.dto.common.StudentIdStudentCode;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final GradeRepository gradeRepository;
    private final CourseUserRepository courseUserRepository;

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
                                .findGradeByStudentId(u.getStudentID(), args.getAssignmentId(), args.getCourseId()).getId())
                        .gradeReviewId(args.getGradeReviewId())
                        .userId(u.getId())
                        .isSeen((byte) 0)
                        .typeNotification(ETypeNotification.ForStudent)
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
                .typeNotification(ETypeNotification.ForStudent)
                .build();
        notification.setIsSeen(false);
        return notificationRepository.save(AuditUtils.createAudit(notification, args.getCurrentUser()));
    }

    // TODO Testing
    @Override
    public long countByUserId(Integer userId) {
        return notificationRepository.countByUserId(userId);
    }

    @Override
    public List<Notification> findAllByUserId(Integer id, Pageable pageable) {
        return notificationRepository.findAllByUserId(id, pageable);
    }

    @Override
    public List<Notification> findAllByUserId(Integer id) {
        return notificationRepository.findAllByUserId(id);
    }

    @Override
    public Notification findByUserIdAndId(Integer userId, Integer id) {
        return notificationRepository.findByUserIdAndId(userId, id);
    }

    @Override
    public void save(Notification notification, String auditor) {
        notificationRepository.save(AuditUtils.updateAudit(notification, auditor));
    }

    @Override
    public void updateBatch(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Override
    public List<Notification> createRequestGradeReviewNotification(String currentUser, GradeReview gradeReview, String reason, Student student) throws NotFoundException {
        var grade = gradeReview.getGrade();
        if (grade == null) throw new NotFoundException("Not found grade for grade review id = " + gradeReview.getId());
        var assignment = grade.getAssigment();
        if (assignment == null) throw new NotFoundException("Not found assignment for grade id = " + grade.getId());
        var course = assignment.getCourse();
        if (course == null) throw new NotFoundException("Not found course for assignment id " + assignment.getId());
        var teachers = courseUserRepository.findUserByCourseIdAndRole(course.getId(), ERole.Teacher.getValue());
        List<Notification> notifications = teachers.stream().map(t -> {
            var notification = Notification.builder()
                    .message(reason)
                    .userId(t.getId())
                    .courseId(course.getId())
                    .gradeId(grade.getId())
                    .gradeReviewId(gradeReview.getId())
                    .senderName(student.getFullName())
                    .isSeen((byte) 0)
                    .typeNotification(ETypeNotification.ForTeacher)
                    .build();
            return AuditUtils.createAudit(notification, currentUser);
        }).toList();
        return notificationRepository.saveAll(notifications);
    }
}
