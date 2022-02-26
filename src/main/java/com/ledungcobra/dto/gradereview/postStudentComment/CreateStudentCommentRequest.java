package com.ledungcobra.dto.gradereview.postStudentComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CreateStudentCommentRequest extends GradeReviewValidatorArgs implements Serializable {
    private String message;

    @Builder
    public CreateStudentCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, String message) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.message = message;
    }
}
