package com.ledungcobra.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentNotificationSingleArgs {
    private String message;
    private Integer courseId;
    private Integer gradeId;
    private Integer gradeReviewId;
    private String studentId;
    private String currentUser;

    public Integer getGradeReviewId() {
        return gradeReviewId == null ? 0 : gradeReviewId;
    }
}
