package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.CourseUser;
import com.ledungcobra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseUserRepository extends JpaRepository<CourseUser, Integer> {

    @Query("select cu.user FROM CourseUser cu where cu.course.id = :courseId and cu.role.id=:role")
    List<User> findUserByCourseIdAndRole(@Param("courseId") Integer courseId,
                                         @Param("role") Integer role);

    @Query("FROM  CourseUser cu where cu.role.id = ?1 and cu.user.id = ?2 and cu.course.id = ?3")
    CourseUser findCourseUserByRoleAndUserIdAndCourseId(Integer role, Integer userId, Integer courseId);

    @Query("FROM  CourseUser cu where cu.user.id = ?1 and cu.course.id = ?2")
    CourseUser findCourseUserByUserIdAndCourseId(Integer userId, Integer courseId);
}