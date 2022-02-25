
package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.CourseStudent;
import com.ledungcobra.course.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Integer> {
    @Query("select cs.student from CourseStudent cs where cs.course.id = :courseId")
    List<Student> findAllStudentInCourse(@Param("courseId") Integer courseId);

    @Query("From CourseStudent  cs where cs.course.id = :courseId")
    List<CourseStudent> findAllByCourseId(@Param("courseId") Integer courseId);

    CourseStudent findByStudentCode(String studentID);
}