package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}