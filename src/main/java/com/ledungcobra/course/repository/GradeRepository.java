package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
}