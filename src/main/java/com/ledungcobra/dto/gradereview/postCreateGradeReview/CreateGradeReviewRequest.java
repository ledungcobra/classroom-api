package com.ledungcobra.dto.gradereview.postCreateGradeReview;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CreateGradeReviewRequest extends GradeReviewValidatorArgs implements Serializable {
    private Integer courseId;
    private Integer gradeId;
    private String currentUser;

    private Float gradeExpect;
    private String reason;

    @Builder
    public CreateGradeReviewRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, Integer courseId1, Integer gradeId1, String currentUser1, Float gradeExpect, String reason) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.courseId = courseId1;
        this.gradeId = gradeId1;
        this.currentUser = currentUser1;
        this.gradeExpect = gradeExpect;
        this.reason = reason;
    }
}
