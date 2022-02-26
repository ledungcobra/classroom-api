package com.ledungcobra.dto.gradereview.deleteStudentDeleteComment;

import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeleteStudentCommentRequest extends GradeReviewValidatorArgs implements Serializable {
    private Integer reviewCommentId;
    private String message;
}
