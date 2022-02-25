package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

    @Query("From Grade  g where g.assigment.course.id = :courseId")
    List<Grade> findByCourseId(@Param("courseId") Integer courseId);

    @Query("From Grade  g where g.assigment.course.id = ?1 and g.student.id = ?2 and g.isFinalized = 1")
    List<Grade> getGradeOfCourseForStudentFinalized(Integer courseId, Integer studentTableId);

    @Query("From Grade  g where g.assigment.course.id = ?1 and g.student.id = ?2")
    List<Grade> getGradeOfCourseForStudentNotFinalized(Integer courseId, Integer studentTableId);


    @Query("FROM Grade g where g.assigment.id = :assignmentId")
    List<Grade> findAllGradeByAssignmentId(@Param("assignmentId") Integer assignmentId);

    @Query("FROM Grade g where g.assigment.id = :assignmentsId and g.assigment.course.id = :courseId and g.mssv = :mssv")
    Grade findGradeByStudentId(@Param("mssv") String mssv,
                               @Param("assignmentsId") Integer assignmentsId,
                               @Param("courseId") Integer courseId);
}