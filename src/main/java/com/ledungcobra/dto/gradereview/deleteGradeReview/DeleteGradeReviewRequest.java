package com.ledungcobra.dto.gradereview.deleteGradeReview;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeleteGradeReviewRequest extends GradeReviewValidatorArgs implements Serializable {

    @Builder
    public DeleteGradeReviewRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser) {
        super(courseId, gradeId, gradeReviewId, currentUser);
    }
}
