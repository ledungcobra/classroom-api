package com.ledungcobra.dto.course.getAllGradesV2;

import com.ledungcobra.course.entity.Grade;
import com.ledungcobra.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeOfAssignmentResponse implements Serializable {
    private String mssv;
    private String name;
    private Float grade;
    private String username;
    private Integer maxGrade;

    public GradeOfAssignmentResponse(Grade grade, User user) {
        this.mssv = grade.getMssv();
        this.name = grade.getStudent() != null ? grade.getStudent().getFullName() : "";
        this.grade = grade.getGradeAssignment();
        this.username = user.getUserName();
        this.maxGrade = grade.getAssigment().getMaxGrade();
    }
}
