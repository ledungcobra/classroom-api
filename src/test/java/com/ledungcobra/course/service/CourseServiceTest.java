package com.ledungcobra.course.service;

import com.ledungcobra.common.OffsetPageable;
import com.ledungcobra.common.PageableBuilder;
import com.ledungcobra.course.entity.Course;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@FlywayTest
@ComponentScan(basePackages = {
        "com.ledungcobra.user",
        "com.ledungcobra.configuration.beans",
        "com.ledungcobra.course",
        "com.ledungcobra.common",
        "com.ledungcobra.configuration.database"
})
@Slf4j
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    public static CreateCourseRequest createCourse_success = CreateCourseRequest.builder()
            .credits(7)
            .currentUser("tanhank2k")
            .description("test")
            .gradeId(0)
            .role(0)
            .title("Schedule")
            .schedule("Schedule")
            .subjectId(0)
            .build();
    ;

    @Test
    void getCoursesByTitleLike() {
        var pageRequest = new OffsetPageable(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "id"));
        List<Course> courses = courseService.getCoursesByTitleLike("%%", pageRequest, "tanhank2k");
        log.info("courses size {}", courses.size());
        assertThat(courses.size()).isEqualTo(7);
    }

    @Test
    void createCourse() {
        Course savedCourse = courseService.createCourse(createCourse_success);
        assertThat(savedCourse).isNotNull();
    }

    @Test
    void countByOwner() {
        var count = courseService.countByOwner("tanhank2k");
        assertThat(count).isEqualTo(7);
    }

    @Test
    void findCourseById() {
        var foundCourse = courseService.findCourseById("tanhank2k", 1);
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getId()).isEqualTo(1);
        assertThat(foundCourse.getTitle()).isNotNull().isNotBlank();
        assertThat(foundCourse.getCreateBy()).isEqualTo("tanhank2k");
        assertThat(foundCourse.getCourseCode()).isNotNull().isNotBlank();
        assertThat(foundCourse.getDescription()).isNotNull().isNotBlank();

    }

    @Test
    void findAssignmentOfsCourseLikeName() {
        var page = PageableBuilder.getPageable(0, 1000, null);
        var assignments = courseService.findAssignmentOfsCourseLikeName("tanhank2k", 1, "%%", page);
        assertThat(assignments).isNotNull();
        assertThat(assignments.size()).isEqualTo(4);
        assignments = courseService.findAssignmentOfsCourseLikeName("tanhank2k", 1, "%tuần%", page);
        assertThat(assignments).isNotNull();
        assertThat(assignments.size()).isEqualTo(4);
    }
}