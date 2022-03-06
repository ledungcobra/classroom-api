package com.ledungcobra.dto.gradereview;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeReviewValidatorArgs implements Serializable {
    @NotNull
    protected Integer courseId;
    @NotNull
    protected Integer gradeId;
    @NotNull
    protected Integer gradeReviewId;
    protected String currentUser;
}
