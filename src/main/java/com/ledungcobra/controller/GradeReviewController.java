package com.ledungcobra.controller;

import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EResult;
import com.ledungcobra.common.ERole;
import com.ledungcobra.common.EStatus;
import com.ledungcobra.course.repository.CourseUserRepository;
import com.ledungcobra.course.repository.GradeRepository;
import com.ledungcobra.course.repository.StudentRepository;
import com.ledungcobra.course.service.CourseService;
import com.ledungcobra.course.service.GradeReviewService;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.GradeResponse;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.GradeReviewResponse;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.StudentResponse;
import com.ledungcobra.dto.gradereview.getGradeReviewComments.ReviewCommentResponse;
import com.ledungcobra.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/grade-review")
@RequiredArgsConstructor
public class GradeReviewController {

    private final UserService userService;
    private final CourseService courseService;
    private final StudentRepository studentRepository;
    private final CourseUserRepository courseUserRepository;
    private final GradeReviewService gradeReviewService;
    private final GradeRepository gradeRepository;

    // TODO Testing
    @GetMapping("")
    @SneakyThrows
    public ResponseEntity<?> getGradeReview(@RequestParam("CourseId") Integer courseId,
                                            @RequestParam("GradeId") Integer gradeId,
                                            @RequestParam("GradeReviewId") Integer gradeReviewId,
                                            @RequestParam("CurrentUser") String currentUser
    ) {

        if (!validateGradeReviewOfUser(currentUser, courseId, ERole.None, false, gradeId, gradeReviewId)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Success)
                    .result(EResult.Successful)
                    .content("")
                    .message("Permission denied for getting grade review ")
                    .build());
        }

        var gradeReview = gradeReviewService.findById(gradeReviewId);
        if (gradeReview == null) return badRequest().build();
        var grade = gradeRepository.findById(gradeId).orElseThrow();
        var response = new GradeReviewResponse(gradeReview);
        response.setGrade(new GradeResponse(grade));
        response.setExerciseName(grade.getAssigment() != null ? grade.getAssigment().getName() : "");
        response.setStudent(new StudentResponse(grade.getStudent()));
        return ok(CommonResponse.builder()
                .result(EResult.Successful)
                .status(EStatus.Success)
                .content(response)
                .message("Get grade review success")
                .build());
    }

    private boolean validateGradeReviewOfUser(String currentUser, Integer courseId,
                                              ERole role, boolean isCheckOwner, Integer gradeId, int gradeReviewId) {
        var user = userService.findByUsername(currentUser);
        if (user == null) return false;

        var course = courseService.findCourseById(currentUser, courseId);
        if (course == null) return false;

        var student = studentRepository.findByStudentId(user.getStudentID());
        switch (role) {
            case None -> {
                var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(user.getId(), courseId);
                if (courseUser == null) return false;

                var gradeReview = gradeReviewService.findById(gradeReviewId);
                if (gradeReview == null ||
                        (courseUser.getRole().getId() == ERole.Student.getValue() &&
                                !Objects.equals(student.getId(), gradeReview.getStudent() != null ? gradeReview.getStudent().getStudentId() : null))
                ) {
                    return false;
                }
            }
            case Teacher -> {
                var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(user.getId(), courseId);
                if (courseUser == null || courseUser.getRole().getId() != ERole.Teacher.getValue()) return false;
            }
            case Student -> {
                var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(user.getId(), courseId);
                if (courseUser == null || courseUser.getRole().getId() != ERole.Student.getValue()) {
                    return false;
                }
                var grade = gradeRepository.findByIdAndCourseId(gradeId, courseId);
                if (grade != null) {
                    return Objects.equals(grade.getMssv(), user.getStudentID());
                }
            }
        }
        if (isCheckOwner) {
            return Objects.equals(course.getCreateBy(), currentUser);
        }
        return true;
    }


    @PostMapping("")
    public ResponseEntity<?> postCreateGradeReview() {
        return null;
    }

    // TODO TEsting
    @GetMapping("/comments")
    public ResponseEntity<?> getGradeReviewComments(@RequestParam("CourseId") Integer courseId,
                                                    @RequestParam("GradeId") Integer gradeId,
                                                    @RequestParam("GradeReviewId") Integer gradeReviewId,
                                                    @RequestParam("CurrentUser") String currentUser) {
        if (!validateGradeReviewOfUser(currentUser, courseId, ERole.None, false, gradeId, gradeReviewId)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Success)
                    .result(EResult.Successful)
                    .content("")
                    .message("Permission denied for getting grade review comments")
                    .build());
        }

        var comments = gradeReviewService.findAllCommentsByGradeReview(gradeReviewId);
        var response = comments.stream().map(ReviewCommentResponse::new).toList();
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content((Serializable) response)
                .message("Get comment for grade review successfully")
                .build());
    }

    @PostMapping("/approval")
    public ResponseEntity<?> postApproveGradeReview() {
        return null;
    }

    @PutMapping("/update")
    public ResponseEntity<?> putUpdateGradeReview() {
        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGradeReview() {
        return null;
    }

    @PostMapping("/teacher-comment")
    public ResponseEntity<?> postTeacherComment() {
        return null;
    }

    @PutMapping("/teacher-comment/update")
    public ResponseEntity<?> putTeacherUpdateComment() {
        return null;
    }

    @DeleteMapping("/teacher-comment/delete")
    public ResponseEntity<?> deleteTeacherTeacherComment() {
        return null;
    }

    @PostMapping("/student-comment")
    public ResponseEntity<?> postStudentComment() {
        return null;
    }

    @PutMapping("/student-comment/update")
    public ResponseEntity<?> putStudentUpdateComment() {
        return null;
    }

    @DeleteMapping("/student-comment/delete")
    public ResponseEntity<?> deleteStudentDeleteComment() {
        return null;
    }

}
