package com.ledungcobra.dto.gradereview.postApproveGradeReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveGradeReviewResponse implements Serializable {
    private Integer gradeReviewId;
    private Byte status;
}
