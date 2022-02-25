package com.ledungcobra.dto.gradereview.GradeReviewResponse;

import com.ledungcobra.course.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponse implements Serializable {
    private Integer id;
    private Integer gradeId;
    private Float grade;
    private Integer maxGrade;
    private Integer gradeReviewId;
    private Float gradeScale;

    public GradeResponse(Grade grade) {
        id = grade.getAssigment().getId();
        gradeId = grade.getId();
        this.grade = grade.getGradeAssignment();
        maxGrade = grade.getAssigment().getMaxGrade();
        gradeReviewId = 0;
        gradeScale = 0f;
    }
}
