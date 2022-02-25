package com.ledungcobra.dto.course.postUpdateAssignmentNormal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGradeSpecificRequestBase implements Serializable {
    private String mssv;
    private float grade;
}
