package com.ledungcobra.dto.gradereview.deleteTeacherTeacherComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeleteTeacherCommentRequest extends GradeReviewValidatorArgs implements Serializable {

    @NotNull
    private Integer reviewCommentId;

    @Builder
    public DeleteTeacherCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, Integer reviewCommentId) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.reviewCommentId = reviewCommentId;
    }
}
