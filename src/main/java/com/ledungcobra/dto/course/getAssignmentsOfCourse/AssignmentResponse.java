package com.ledungcobra.dto.course.getAssignmentsOfCourse;

import com.ledungcobra.course.entity.Assignment;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponse implements Serializable {
    private Integer id;
    private Integer courseId;
    private String name;
    private String description;
    private Integer maxGrade;
    private Integer order;

    public AssignmentResponse(Assignment data) {
        id = data.getId();
        courseId = data.getCourse().getId();
        name = data.getName();
        description = data.getDescription();
        maxGrade = data.getMaxGrade();
        order = data.getOrder();
    }
}
