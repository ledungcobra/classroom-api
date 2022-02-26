package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.*;
import com.ledungcobra.dto.common.UpdateGradeSpecificArgs;
import com.ledungcobra.dto.course.createNewAssignment.CreateNewAssignmentArgs;
import com.ledungcobra.dto.course.getAllGrades.GradeOfCourseResponse;
import com.ledungcobra.dto.course.getAllGradesV2.GradeOfAssignmentResponse;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeNormalRequest;
import com.ledungcobra.dto.course.updateAssigment.UpdateAssignmentsRequest;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CourseService {

    List<Course> getCoursesByTitleLike(String title, Pageable pageable, String username);

    Course createCourse(CreateCourseRequest request);

    long countByOwner(String owner);

    Course findCourseById(String currentUser, Integer id);

    List<Assignment> findAssignmentOfsCourseLikeName(String currentUser, Integer courseId, String name, Pageable pageable);

    Assignment findAssigmentById(Integer courseId, Integer assignmentId);

    Assignment updateAssignment(UpdateAssignmentsRequest assignment, String currentUser, Integer assignmentId) throws NotFoundException;

    void deleteAssignment(Integer assignmentId) throws NotFoundException;

    List<GradeOfCourseResponse> getAllGrades(Integer courseId);

    GradeOfCourseResponse getGradeOfCourseByStudentTableId(Integer courseId, Integer id) throws NotFoundException;

    List<GradeOfAssignmentResponse> getAllGradeAssignment(Integer assignmentId);


    Grade saveGrade(Grade grade, String currentUser, Integer assignmentsId, Integer courseId);

    Student createNewStudent(String mssv, String auditor);

    void updateStudentsInCourse(List<Student> studentList, Integer courseId, String currentUser);

    boolean updateGradeNormal(UpdateGradeNormalRequest request, Integer assignmentsId, Integer courseId);

    boolean updateGradeSpecific(UpdateGradeSpecificArgs args);

    Integer findGradeByCourseIdAndAssignmentId(String mssv, Integer courseId, Integer assignmentsId);

    List<UserResponse> getTeachers(Integer courseId);

    List<UserResponse> getStudents(Integer courseId);

    List<GradeOfCourseResponse> findAllGradeOfCourse(Integer id);

    List<Course> findAllCourses();

    boolean addMemberIntoCourse(User user, Integer role, Integer id);

    boolean addMemberIntoCourse(Integer inviteeId, String username, Integer role, Integer id);

    void updateRole(CourseUser courseUser, Integer role, String currentUser);

    Assignment createAssignment(CreateNewAssignmentArgs args);

    List<Integer> getTeacherIds(Integer courseId);
}
