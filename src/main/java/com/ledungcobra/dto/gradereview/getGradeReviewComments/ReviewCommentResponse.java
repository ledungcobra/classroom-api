package com.ledungcobra.dto.gradereview.getGradeReviewComments;

import com.ledungcobra.course.entity.ReviewComment;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.StudentResponse;
import com.ledungcobra.dto.user.register.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommentResponse implements Serializable {
    private Integer id;
    private String message;
    private Integer studentId;
    private Integer teacherId;
    private Integer gradeReviewId;
    private StudentResponse student;
    private UserResponse teacher;

    public ReviewCommentResponse(ReviewComment reviewComment) {
        id = reviewComment.getId();
        message = reviewComment.getMessage();
        studentId = reviewComment.getStudent() != null ? reviewComment.getStudent().getId() : 0;
        teacherId = reviewComment.getTeacher() != null ? reviewComment.getTeacher().getId() : 0;
        gradeReviewId = reviewComment.getGradeReview() != null ? reviewComment.getGradeReview().getId() : 0;
        if (reviewComment.getStudent() != null) {
            student = new StudentResponse(reviewComment.getStudent());
        } else if (reviewComment.getTeacher() != null) {
            teacher = new UserResponse(reviewComment.getTeacher());
        }
    }
}
