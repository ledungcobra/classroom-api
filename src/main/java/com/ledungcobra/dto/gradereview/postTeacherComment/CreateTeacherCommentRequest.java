package com.ledungcobra.dto.gradereview.postTeacherComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class CreateTeacherCommentRequest extends GradeReviewValidatorArgs implements Serializable {

    private String message;

    @Builder
    public CreateTeacherCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, String message) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.message = message;
    }
}
