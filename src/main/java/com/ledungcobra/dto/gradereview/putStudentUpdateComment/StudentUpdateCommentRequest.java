package com.ledungcobra.dto.gradereview.putStudentUpdateComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StudentUpdateCommentRequest extends GradeReviewValidatorArgs implements Serializable {

    private Integer reviewCommentId;
    private String message;

    @Builder
    public StudentUpdateCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, Integer reviewCommentId, String message) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.reviewCommentId = reviewCommentId;
        this.message = message;
    }
}
