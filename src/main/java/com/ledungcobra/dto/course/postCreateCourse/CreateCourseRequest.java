package com.ledungcobra.dto.course.postCreateCourse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseRequest implements Serializable {

    private int subjectId;
    private int gradeId;
    private String title;
    private String description;
    private int credits;
    private int role;
    private String schedule;
    private String currentUser;
}
