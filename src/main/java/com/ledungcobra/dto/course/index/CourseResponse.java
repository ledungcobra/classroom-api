package com.ledungcobra.dto.course.index;

import com.ledungcobra.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
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
