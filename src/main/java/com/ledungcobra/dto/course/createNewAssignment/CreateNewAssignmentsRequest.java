package com.ledungcobra.dto.course.createNewAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewAssignmentsRequest implements Serializable {

    @NotNull
    private Integer courseId;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Integer maxGrade;
    private String currentUser;
}
