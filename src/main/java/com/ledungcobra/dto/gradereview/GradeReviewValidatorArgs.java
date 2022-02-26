package com.ledungcobra.dto.gradereview;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeReviewValidatorArgs implements Serializable {
    protected Integer courseId;
    protected Integer gradeId;
    protected Integer gradeReviewId;
    protected String currentUser;

}
