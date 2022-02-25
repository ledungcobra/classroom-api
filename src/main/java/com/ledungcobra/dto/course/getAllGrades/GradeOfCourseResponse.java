package com.ledungcobra.dto.course.getAllGrades;

import com.ledungcobra.course.entity.Grade;
import com.ledungcobra.course.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeOfCourseResponse implements Serializable {
    private Integer id;
    private String mssv;
    private String name;
    private List<GradeSimpleResponse> grades;

    public GradeOfCourseResponse(Student student, List<Grade> grades) {
        this.id = student.getId();
        this.mssv = student.getStudentId();
        this.name = student.getFullName();
        this.grades = grades.stream().map(GradeSimpleResponse::new).toList();
    }
}
