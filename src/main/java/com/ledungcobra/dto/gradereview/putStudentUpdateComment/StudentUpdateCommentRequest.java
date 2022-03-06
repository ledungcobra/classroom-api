package com.ledungcobra.dto.gradereview.putStudentUpdateComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StudentUpdateCommentRequest extends GradeReviewValidatorArgs implements Serializable {

    @NotNull
    private Integer reviewCommentId;

    @NotNull
    @NotBlank
    private String message;

    @Builder
    public StudentUpdateCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, Integer reviewCommentId, String message) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.reviewCommentId = reviewCommentId;
        this.message = message;
    }
}
