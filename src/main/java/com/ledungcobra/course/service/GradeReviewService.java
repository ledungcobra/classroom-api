package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.GradeReview;
import com.ledungcobra.course.entity.ReviewComment;
import com.ledungcobra.exception.NotFoundException;

import java.util.List;

public interface GradeReviewService {
    GradeReview findById(int gradeReviewId);

    List<ReviewComment> findAllCommentsByGradeReview(Integer gradeReviewId);

    GradeReview createGradeReview(Integer gradeId, Float gradeExpect, Integer id, String reason, String currentUser);

    void approveGradeReview(Byte approvalStatus, Integer gradeReviewId, String currentUser) throws NotFoundException;

    GradeReview findGradeReviewByIdAndCreateByAndStatus(Integer gradeReviewId, String userName, int status);

    GradeReview update(GradeReview gradeReview, String currentUser);

    ReviewComment update(ReviewComment reviewComment, String currentUser);

    void delete(GradeReview gradeReview);

    void delete(ReviewComment reviewComment);

    ReviewComment createReviewComment(Integer gradeReviewId, String message, Integer teacherId, int studentId, String currentUser);

    ReviewComment findReviewCommentById(Integer reviewCommentId);
}
