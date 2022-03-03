package com.ledungcobra.course.service;

import com.ledungcobra.common.PageableBuilder;
import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.course.repository.NotificationRepository;
import com.ledungcobra.course.repository.StudentRepository;
import com.ledungcobra.dto.common.CreateStudentNotificationSingleArgs;
import com.ledungcobra.dto.common.CreateStudentNotificationsArgs;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"default", "test"})
@FlywayTest
@ComponentScan(basePackages = {
        "com.ledungcobra.user",
        "com.ledungcobra.configuration.beans",
        "com.ledungcobra.course",
        "com.ledungcobra.common",
        "com.ledungcobra.configuration.database"
})
@Slf4j
@Transactional
class NotificationServiceImplTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    CourseService courseService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    StudentRepository userService;

    @Test
    void createStudentNotifications() {
        var students = List.of("1", "2", "3");
        var count = notificationRepository.count();
        var notifications = notificationService
                .createStudentNotifications(CreateStudentNotificationsArgs.builder()
                        .courseId(1)
                        .assignmentId(1)
                        .students(students.stream().map(userService::findByStudentId).toList())
                        .message(String.format("%s đã trả điểm cho bài tập %s", "test", "test"))
                        .build());

        assertThat(notifications.size()).isEqualTo(students.size());
        assertThat(notificationRepository.count()).isEqualTo(count + students.size());
    }

    @Test
    void createStudentNotification() {
        var count = notificationRepository.count();
        var args = CreateStudentNotificationSingleArgs.builder()
                .courseId(1)
                .studentId("1")
                .gradeId(courseService.findGradeByCourseIdAndAssignmentId("1", 1, 1))
                .message(String.format("%s đã trả điểm cho bài tập %s", "test", "test"))
                .currentUser("tanhank2k")
                .build();

        Notification notification = notificationService.createStudentNotification(args);
        assertThat(notification).isNotNull();
        assertThat(notification.getMessage()).isEqualTo(args.getMessage());
        assertThat(notification.getCourseId()).isEqualTo(args.getCourseId());
        assertThat(notification.getCreateBy()).isEqualTo(args.getCurrentUser());
        assertThat(notificationRepository.count()).isEqualTo(count + 1);
    }

    @Test
    void countByUserId() {
        long count = notificationRepository.countByUserId(15);
        assertThat(count).isEqualTo(11);
    }

    @Test
    void findAllByUserId() {
        var notifications = notificationService.findAllByUserId(15);
        assertThat(notifications).hasSize(11);
    }

    @Test
    void findAllByUserIdV2() {
        var resultCount = 9;
        var page = PageableBuilder.getPageable(0, resultCount, "-CreateOn");
        var notifications = notificationService.findAllByUserId(15, page);
        assertThat(notifications).hasSize(resultCount);
    }

    @Test
    void findAllByUserIdV3() {
        var resultCount = 9;
        var page = PageableBuilder.getPageable(0, resultCount, "-CreateOn");
        var notifications = notificationService.findAllByUserId(15, page);
        assertThat(notifications).hasSize(resultCount);
    }

}