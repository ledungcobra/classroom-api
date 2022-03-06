package com.ledungcobra.dto.gradereview.postTeacherComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class CreateTeacherCommentRequest extends GradeReviewValidatorArgs implements Serializable {

    @NotNull
    @NotBlank
    private String message;

    @Builder
    public CreateTeacherCommentRequest(Integer courseId, Integer gradeId, Integer gradeReviewId, String currentUser, String message) {
        super(courseId, gradeId, gradeReviewId, currentUser);
        this.message = message;
    }
}
