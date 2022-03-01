package com.ledungcobra.dto.course.getCourseById;

import com.ledungcobra.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseResponse implements Serializable {

    private int id;
    private int gradeId;
    private String title;
    private String classCode;
    private int credit;
    private String description;
    private String schedule;
    private String owner;
    private String createUsername;
    private Integer role;

    public CourseResponse(Course course) {
        id = course.getId();
        gradeId = course.getGradeId();
        title = course.getTitle();
        classCode = course.getCourseCode();
        credit = course.getCredits();
        description = course.getDescription();
        schedule = course.getSchedule();
        owner = course.getCreateBy();
        createUsername = course.getCreateBy();
    }
}
