package com.ledungcobra.course.repository;

import com.ledungcobra.course.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {
}