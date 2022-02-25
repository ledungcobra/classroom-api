package com.ledungcobra.dto.course.postUpdateAssignmentNormal;

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
public class UpdateGradeNormalRequest implements Serializable {
    private Boolean isFinalized;
    private String currentUser;
    private List<UpdateGradeSpecificRequestBase> scores;
}
