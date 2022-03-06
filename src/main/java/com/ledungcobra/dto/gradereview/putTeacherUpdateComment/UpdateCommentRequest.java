package com.ledungcobra.dto.gradereview.putTeacherUpdateComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UpdateCommentRequest extends GradeReviewValidatorArgs implements Serializable {

    @NotNull
    @NotBlank
    private String message;

    @NotNull
    private  Integer reviewCommentId;

    @Builder
    public UpdateCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, String message, Integer reviewCommentId) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.message = message;
        this.reviewCommentId = reviewCommentId;
    }
}
