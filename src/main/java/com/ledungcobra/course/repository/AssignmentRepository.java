package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query("FROM Assignment a where a.course.id=?1")
    List<Assignment> findByCourseId(Integer id);

    @Query("select count(a) FROM Assignment a where a.course.id = ?1")
    Integer countAssignmentByCourseId(Integer courseId);
}