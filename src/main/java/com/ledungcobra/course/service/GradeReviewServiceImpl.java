package com.ledungcobra.course.service;

import com.ledungcobra.course.entity.GradeReview;
import com.ledungcobra.course.entity.ReviewComment;
import com.ledungcobra.course.repository.GradeReviewRepository;
import com.ledungcobra.course.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeReviewServiceImpl implements GradeReviewService {

    private final GradeReviewRepository gradeReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public GradeReview findById(int gradeReviewId) {
        return gradeReviewRepository.findById(gradeReviewId).orElse(null);
    }

    @Override
    public List<ReviewComment> findAllCommentsByGradeReview(Integer gradeReviewId) {
        return reviewCommentRepository.findAllbyGradeReviewId(gradeReviewId);
    }
}
