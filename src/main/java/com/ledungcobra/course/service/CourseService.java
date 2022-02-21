package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.Assignment;
import com.ledungcobra.course.entity.Course;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CourseService {

    List<Course> getCoursesByTitleLike(String title, Pageable pageable, String username);
    Course createCourse(CreateCourseRequest request);

    long countByOwner(String owner);

    Course findCourseById(String currentUser, Integer id);

    List<Assignment> findAssignmentOfsCourseLikeName(String currentUser, Integer courseId, String name, Pageable pageable);
}
