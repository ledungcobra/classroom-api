package com.ledungcobra.dto.gradereview.putUpdateGradeReview;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UpdateGradeReviewRequest extends GradeReviewValidatorArgs implements Serializable {

    @Nonnegative
    private Float gradeExpect;

    @NotNull
    private String reason;

    @Builder
    public UpdateGradeReviewRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, Float gradeExpect, String reason) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.gradeExpect = gradeExpect;
        this.reason = reason;
    }
}
