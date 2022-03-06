package com.ledungcobra.dto.course.postUpdateAssignmentNormal;

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
public class UpdateGradeSpecificRequestBase implements Serializable {
    @NotNull
    @NotBlank
    private String mssv;
    private float grade;
}
