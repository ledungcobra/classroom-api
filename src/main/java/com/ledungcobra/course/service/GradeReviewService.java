package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.GradeReview;
import com.ledungcobra.course.entity.ReviewComment;

import java.util.List;

public interface GradeReviewService {
    GradeReview findById(int gradeReviewId);

    List<ReviewComment> findAllCommentsByGradeReview(Integer gradeReviewId);
}
