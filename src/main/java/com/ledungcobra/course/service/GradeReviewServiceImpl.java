package com.ledungcobra.course.service;

import com.ledungcobra.common.AuditUtils;
import com.ledungcobra.common.EGradeReviewStatus;
import com.ledungcobra.course.entity.Grade;
import com.ledungcobra.course.entity.GradeReview;
import com.ledungcobra.course.entity.ReviewComment;
import com.ledungcobra.course.entity.Student;
import com.ledungcobra.course.repository.GradeRepository;
import com.ledungcobra.course.repository.GradeReviewRepository;
import com.ledungcobra.course.repository.ReviewCommentRepository;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeReviewServiceImpl implements GradeReviewService {

    private final GradeReviewRepository gradeReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final EntityManager entityManager;
    private final GradeRepository gradeRepository;

    @Override
    public GradeReview findById(int gradeReviewId) {
        return gradeReviewRepository.findById(gradeReviewId).orElse(null);
    }

    @Override
    public List<ReviewComment> findAllCommentsByGradeReview(Integer gradeReviewId) {
        return reviewCommentRepository.findAllbyGradeReviewId(gradeReviewId);
    }

    @Override
    public GradeReview createGradeReview(Integer gradeId, Float gradeExpect, Integer studentId, String reason, String currentUser) {
        var gradeReview = GradeReview.builder()
                .grade(entityManager.getReference(Grade.class, gradeId))
                .gradeExpect(gradeExpect)
                .message(reason)
                .student(entityManager.getReference(Student.class, studentId))
                .status(EGradeReviewStatus.Pending.getValue())
                .build();
        return gradeReviewRepository.save(AuditUtils.createAudit(gradeReview, currentUser));
    }

    @Override
    public void approveGradeReview(Byte approvalStatus, Integer gradeReviewId, String currentUser) throws NotFoundException {
        var gradeReview = gradeReviewRepository.findById(gradeReviewId).orElseThrow(() -> new NotFoundException("Not found grade review"));
        gradeReview.setStatus(Integer.valueOf(approvalStatus));
        gradeReviewRepository.save(AuditUtils.updateAudit(gradeReview, currentUser));

        if (approvalStatus == EGradeReviewStatus.Approve.getValue()) {
            var grade = gradeReview.getGrade();
            grade.setGradeAssignment(gradeReview.getGradeExpect());
            gradeRepository.save(AuditUtils.updateAudit(grade, currentUser));
        }

    }

    @Override
    public GradeReview findGradeReviewByIdAndCreateByAndStatus(Integer gradeReviewId, String createBy, int status) {
        return gradeReviewRepository.findByIdAndCreateByAndStatus(gradeReviewId, createBy, status);
    }

    @Override
    public GradeReview update(GradeReview gradeReview, String currentUser) {
        return gradeReviewRepository.save(AuditUtils.updateAudit(gradeReview, currentUser));
    }

    @Override
    public ReviewComment update(ReviewComment reviewComment, String currentUser) {
        return reviewCommentRepository.save(AuditUtils.updateAudit(reviewComment, currentUser));
    }

    @Override
    public void delete(GradeReview gradeReview) {
        gradeReviewRepository.delete(gradeReview);
    }

    @Override
    public void delete(ReviewComment reviewComment) {
        reviewCommentRepository.delete(reviewComment);
    }

    /**
     * @param gradeReviewId
     * @param message
     * @param teacherId     pass 0 if it is student comment
     * @param studentId     pass 0 if it is teacher comment
     * @param currentUser
     * @return
     */
    @Override
    public ReviewComment createReviewComment(Integer gradeReviewId, String message, Integer teacherId, int studentId, String currentUser) {
        var reviewComment = ReviewComment.builder()
                .gradeReview(entityManager.getReference(GradeReview.class, gradeReviewId))
                .student(studentId == 0 ? null : entityManager.getReference(Student.class, studentId))
                .teacher(teacherId == 0 ? null : entityManager.getReference(User.class, teacherId))
                .message(message)
                .build();
        return reviewCommentRepository.save(AuditUtils.createAudit(reviewComment, currentUser));
    }

    @Override
    public ReviewComment findReviewCommentById(Integer reviewCommentId) {
        return reviewCommentRepository.findById(reviewCommentId).orElse(null);
    }
}
