package com.ledungcobra.dto.gradereview.GradeReviewResponse;

import com.ledungcobra.course.entity.GradeReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeReviewResponse implements Serializable {
    private Integer id;
    private Float gradeExpect;
    private String message;
    private String studentId;
    private String mssv;
    private String exerciseName;
    private Integer gradeId;
    private GradeResponse grade;
    private StudentResponse student;
    private Integer status;

    public GradeReviewResponse(GradeReview gradeReview) {
        id = gradeReview.getId();
        gradeExpect = gradeReview.getGradeExpect();
        message = gradeReview.getMessage();
        studentId = gradeReview.getStudent() != null ? gradeReview.getStudent().getStudentId(): "";
        mssv = studentId;
        gradeId = gradeReview.getGrade() != null? gradeReview.getGrade().getId():0;
        status = gradeReview.getStatus();

    }
}
