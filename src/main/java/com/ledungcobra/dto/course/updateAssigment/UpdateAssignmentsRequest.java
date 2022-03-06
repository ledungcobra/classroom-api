package com.ledungcobra.dto.course.updateAssigment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAssignmentsRequest implements Serializable {
    @NotNull
    @NotBlank
    private String name;
    private String description;

    @NotNull
    @Nonnegative
    private Integer maxGrade;

    private String currentUser;
}
