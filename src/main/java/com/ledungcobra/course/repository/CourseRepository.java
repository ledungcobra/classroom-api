package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.Assignment;
import com.ledungcobra.course.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {


    @Query(value = "select * from course c " +
            "where c.title like :title and c.id in ((select  c1.Id from course c1 inner join  courseuser cu on c1.Id = cu.CourseId" +
            "                                               inner join users u on u.Id = cu.UserId " +
            "                                                           where u.UserName = :username " +
            "                                               union" +
            "                                        select   c2.Id from course c2 inner join coursestudent c on c2.Id = c.CourseId inner join users u on u.Id = c.StudentId" +
            "                                       where u.UserName = :username" +
            "))", nativeQuery = true)
    List<Course> getCoursesByTitleLike(@Param("title") String title,
                                       @Param("username") String username,
                                       Pageable pageable);

    @Query(value = "SELECT count(*) from " +
            " (select  c1.Id from course c1 inner join  courseuser cu on c1.Id = cu.CourseId" +
            " inner join users u on u.Id = cu.UserId " +
            " where u.UserName = :owner " +
            " union" +
            " select   c2.Id from course c2 inner join coursestudent c on c2.Id = c.CourseId inner join users u on u.Id = c.StudentId" +
            " where u.UserName = :owner" +
            ") a", nativeQuery = true)
    long countByOwner(@Param("owner") String owner);

    @Query(value = "SELECT * from course c0 where c0.Id = :id and" +
            "            c0.Id in" +
            "             (select  c1.Id from course c1 inner join  courseuser cu on c1.Id = cu.CourseId " +
            "             inner join users u on u.Id = cu.UserId  " +
            "             where u.UserName = :owner " +
            "             union " +
            "             select   c2.Id from course c2 inner join coursestudent c on c2.Id = c.CourseId inner join users u on u.Id = c.StudentId " +
            "             where u.UserName = :owner ) " +
            "limit 1", nativeQuery = true)
    Course findCourseByIdAndUserName(@Param("owner") String currentUser, @Param("id") Integer id);

    @Query("select distinct c.assignments from Course c where c.title like :name and c.id in " +
            "(select c1.id from Course c1  inner join c1.users as u1 where c1.id = :id and u1.userName = :owner) or " +
            "c.id in (select c2.id from Course c2  inner join c2.students as s where (c2.id = :id))")
    List<Assignment> findAssignmentsOfCourseLikeName(@Param("owner") String currentUser, @Param("id") Integer courseId, @Param("name") String name, Pageable pageable);

}