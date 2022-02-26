package com.ledungcobra.dto.gradereview.postApproveGradeReview;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApprovalGradeReviewRequest extends GradeReviewValidatorArgs implements Serializable{
    private Byte approvalStatus;

    @Builder
    public ApprovalGradeReviewRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, Byte approvalStatus) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.approvalStatus = approvalStatus;
    }
}
