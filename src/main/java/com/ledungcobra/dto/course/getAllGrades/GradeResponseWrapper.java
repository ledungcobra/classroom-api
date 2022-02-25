package com.ledungcobra.dto.course.getAllGrades;

import com.ledungcobra.dto.course.getAssignmentsOfCourse.AssignmentResponse;
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
public class GradeResponseWrapper implements Serializable {
    private List<AssignmentResponse> header;
    private List<GradeOfCourseResponse> scores;
    private Integer total;
}
