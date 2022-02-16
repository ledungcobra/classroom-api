
package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Integer> {
}