package com.ledungcobra.course.service;

import com.ledungcobra.common.AuditUtils;
import com.ledungcobra.course.entity.Assignment;
import com.ledungcobra.course.entity.Course;
import com.ledungcobra.course.repository.CourseRepository;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getCoursesByTitleLike(String title, Pageable pageable, String username) {
        return courseRepository.getCoursesByTitleLike(title, username, pageable);
    }

    @Override
    public Course createCourse(CreateCourseRequest request) {
        var course = Course.builder()
                .schedule(request.getSchedule())
                .description(request.getDescription())
                .gradeId(request.getGradeId())
                .courseCode(UUID.randomUUID().toString())
                .title(request.getTitle())
                .credits(request.getCredits())

                .build();
        return courseRepository.save(AuditUtils.createAudit(course, request.getCurrentUser()));
    }

    @Override
    public long countByOwner(String owner) {
        return courseRepository.countByOwner(owner);
    }

    @Override
    public Course findCourseById(String currentUser, Integer id) {
        return courseRepository.findCourseByIdAndUserName(currentUser, id);
    }

    @Override
    public List<Assignment> findAssignmentOfsCourseLikeName(String currentUser, Integer courseId, String name, Pageable pageable) {
        return courseRepository.findAssignmentsOfCourseLikeName(currentUser, courseId,name,pageable);
    }

}
