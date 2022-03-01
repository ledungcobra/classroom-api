package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query("FROM Assignment a where a.course.id=?1")
    List<Assignment> findByCourseId(Integer id);

    @Query("select count(a) FROM Assignment a where a.course.id = ?1")
    Integer countAssignmentByCourseId(Integer courseId);

    @Query("FROM Assignment a where a.course.id = ?1 and a.name like ?2")
    List<Assignment> findByCourseIdAndNameLike(Integer courseId, String name, Pageable page);

    @Query("SELECT count(a) from Assignment  a where a.course.id = ?1")
    long countByCourseId(Integer courseId);
}