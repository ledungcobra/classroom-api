package com.ledungcobra.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ledungcobra.course.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentNotificationsArgs {
    private Integer courseId;
    private List<Student> students;
    private String message;
    private String currentUser;

    @JsonIgnore
    private Integer gradeId;
    @JsonIgnore
    private Integer gradeReviewId;
    private Integer assignmentId;

    public Integer getGradeReviewId() {
        return gradeReviewId == null ? 0 : gradeReviewId;
    }
}
