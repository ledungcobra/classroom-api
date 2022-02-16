package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.AssignmentStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentStudentRepository extends JpaRepository<AssignmentStudent, Integer> {
}