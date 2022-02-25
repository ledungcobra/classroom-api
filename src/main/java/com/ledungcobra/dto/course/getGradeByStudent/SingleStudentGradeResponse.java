package com.ledungcobra.dto.course.getGradeByStudent;

import com.ledungcobra.dto.course.getAllGrades.GradeOfCourseResponse;
import com.ledungcobra.dto.getAssignmentsOfCourse.AssignmentResponse;
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
public class SingleStudentGradeResponse implements Serializable {
    private List<AssignmentResponse> header;
    private GradeOfCourseResponse scores;
    private Integer total;
}
