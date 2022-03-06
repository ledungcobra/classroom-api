package com.ledungcobra.dto.course.postUpdateAssignmentNormal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGradeNormalRequest implements Serializable {
    @NotNull
    private Boolean isFinalized;
    private String currentUser;
    @NotNull
    private List<UpdateGradeSpecificRequestBase> scores;
}
