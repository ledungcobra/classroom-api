package com.ledungcobra.dto.course.createNewAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewAssignmentsRequest implements Serializable {
    private Integer courseId;
    private String name;
    private String description;
    private Integer maxGrade;
    private String currentUser;
}
