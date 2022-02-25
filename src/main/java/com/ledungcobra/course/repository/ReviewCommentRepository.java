package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {
    @Query("FROM ReviewComment rc where rc.gradeReview.id = ?1")
    List<ReviewComment> findAllbyGradeReviewId(Integer gradeReviewId);
}