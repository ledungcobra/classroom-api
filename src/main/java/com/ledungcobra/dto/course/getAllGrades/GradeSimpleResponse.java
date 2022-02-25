package com.ledungcobra.dto.course.getAllGrades;

import com.ledungcobra.course.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeSimpleResponse implements Serializable {

    private Integer id;
    private Integer gradeId;
    private Integer gradeReviewId;
    private Float grade;
    private Integer maxGrade;

    public GradeSimpleResponse(Grade grade) {
        id = grade.getAssigment().getId();
        gradeId = grade.getId();
        this.grade = grade.getGradeAssignment();
        maxGrade = grade.getAssigment().getMaxGrade();
        gradeReviewId = grade.getGradeReview() == null ? 0 : grade.getGradeReview().getId();
    }
}
