package com.ledungcobra.controller;

import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.websocket.MessageBody;
import com.ledungcobra.configuration.websocket.WsMessageController;
import com.ledungcobra.configuration.websocket.WsNotificationController;
import com.ledungcobra.course.entity.Grade;
import com.ledungcobra.course.entity.GradeReview;
import com.ledungcobra.course.entity.ReviewComment;
import com.ledungcobra.course.entity.Student;
import com.ledungcobra.course.repository.CourseUserRepository;
import com.ledungcobra.course.repository.GradeRepository;
import com.ledungcobra.course.repository.StudentRepository;
import com.ledungcobra.course.service.CourseService;
import com.ledungcobra.course.service.GradeReviewService;
import com.ledungcobra.course.service.NotificationService;
import com.ledungcobra.dto.common.CreateStudentNotificationSingleArgs;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.GradeResponse;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.GradeReviewResponse;
import com.ledungcobra.dto.gradereview.GradeReviewResponse.StudentResponse;
import com.ledungcobra.dto.gradereview.GradeReviewValidatorArgs;
import com.ledungcobra.dto.gradereview.deleteGradeReview.DeleteGradeReviewRequest;
import com.ledungcobra.dto.gradereview.deleteStudentDeleteComment.DeleteStudentCommentRequest;
import com.ledungcobra.dto.gradereview.deleteTeacherTeacherComment.DeleteTeacherCommentRequest;
import com.ledungcobra.dto.gradereview.getGradeReviewComments.ReviewCommentResponse;
import com.ledungcobra.dto.gradereview.postApproveGradeReview.ApprovalGradeReviewRequest;
import com.ledungcobra.dto.gradereview.postApproveGradeReview.ApproveGradeReviewResponse;
import com.ledungcobra.dto.gradereview.postCreateGradeReview.CreateGradeReviewRequest;
import com.ledungcobra.dto.gradereview.postStudentComment.CreateStudentCommentRequest;
import com.ledungcobra.dto.gradereview.postTeacherComment.CreateTeacherCommentRequest;
import com.ledungcobra.dto.gradereview.putStudentUpdateComment.StudentUpdateCommentRequest;
import com.ledungcobra.dto.gradereview.putTeacherUpdateComment.UpdateCommentRequest;
import com.ledungcobra.dto.gradereview.putUpdateGradeReview.UpdateGradeReviewRequest;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/grade-review")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
@Slf4j
public class GradeReviewController {

    private final UserService userService;
    private final CourseService courseService;
    private final StudentRepository studentRepository;
    private final CourseUserRepository courseUserRepository;
    private final GradeReviewService gradeReviewService;
    private final GradeRepository gradeRepository;
    private final JwtUtils jwtUtils;
    private final NotificationService notificationService;
    private final WsMessageController wsMessageController;
    private final WsNotificationController wsNotificationController;


    @GetMapping("")
    @SneakyThrows
    public ResponseEntity<?> getGradeReview(@RequestParam("CourseId") Integer courseId, @RequestParam("GradeId") Integer gradeId, @RequestParam("gradeReviewId") Integer gradeReviewId, @RequestParam("CurrentUser") String currentUser) {

        if (!validateGradeReviewOfUser(currentUser, courseId, ERole.None, false, gradeId, gradeReviewId)) {
            return ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).content("").message("Permission denied for getting grade review ").build());
        }

        var gradeReview = gradeReviewService.findById(gradeReviewId);
        if (gradeReview == null) return badRequest().build();
        var grade = gradeRepository.findById(gradeId).orElseThrow();
        var response = new GradeReviewResponse(gradeReview);
        response.setGrade(new GradeResponse(grade));
        response.setExerciseName(grade.getAssigment() != null ? grade.getAssigment().getName() : "");
        response.setStudent(new StudentResponse(grade.getStudent()));
        return ok(CommonResponse.builder().result(EResult.Successful).status(EStatus.Success).content(response).message("Get grade review success").build());
    }

    private boolean validateGradeReviewOfUser(String currentUser, GradeReviewValidatorArgs validatorArgs, ERole role, boolean isCheckOwner) {
        return validateGradeReviewOfUser(currentUser, validatorArgs.getCourseId(), role, isCheckOwner, validatorArgs.getGradeId(), validatorArgs.getGradeReviewId());
    }

    private boolean validateGradeReviewOfUser(String currentUser, Integer courseId, ERole role, boolean isCheckOwner, Integer gradeId, int gradeReviewId) {
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
                if (gradeReview == null || (courseUser.getRole().getId() == ERole.Student.getValue() && !Objects.equals(student.getId(), gradeReview.getStudent() != null ? gradeReview.getStudent().getId() : null))) {
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
    public ResponseEntity<?> postCreateGradeReview(@RequestBody CreateGradeReviewRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        var badRequest = ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error)
                .content("").message("Create grade review failed!!").build());
        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Student, false, request.getGradeId(), 0) || user == null) {
            return badRequest;
        }
        var student = studentRepository.findByStudentId(user.getStudentID());
        if (student == null) {
            return badRequest;
        }
        var gradeReview = gradeReviewService.createGradeReview(request.getGradeId(), request.getGradeExpect(), student.getId(), request.getReason(), currentUser);
        var grade = gradeReview.getGrade();
        var assignment = grade.getAssigment();
        var assignmentName = assignment != null ? assignment.getName() : "";
        var notificationMsg = String.format("%s create new grade review for %s",
                (gradeReview.getStudent() != null ? gradeReview.getStudent().getFullName() : ""), assignmentName
        );
        var notifications = notificationService
                .createRequestGradeReviewNotification(currentUser, gradeReview, notificationMsg, student);
        wsNotificationController.sendNotification(notifications);

        var response = new GradeReviewResponse(gradeReview);
        response.setGrade(new GradeResponse(gradeReview.getGrade()));
        response.setExerciseName(gradeReview.getGrade().getAssigment() != null ? gradeReview.getGrade().getAssigment().getName() : "");
        response.setStudent(new StudentResponse(student));
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message("Create new grade review successfully")
                .content(response)
                .build());
    }

    @PostMapping("/approval")
    public ResponseEntity<?> postApproveGradeReview(@RequestBody ApprovalGradeReviewRequest request,
                                                    HttpServletRequest httpServletRequest
    ) throws NotFoundException {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        var badRequest = ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error)
                .content("").message("Approve grade review fail").build());
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Teacher, false, request.getGradeId(), request.getGradeReviewId())) {
            return badRequest;
        }
        gradeReviewService.approveGradeReview(request.getApprovalStatus(), request.getGradeReviewId(), request.getCurrentUser());
        var res = ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message(String.format("Update sattus for grade review id = %s successfully", request.getGradeReviewId()))
                .content(ApproveGradeReviewResponse.builder()
                        .gradeReviewId(request.getGradeReviewId())
                        .status(request.getApprovalStatus())
                        .build())
                .build());
        GradeReview gradeReview = gradeReviewService.findById(request.getGradeReviewId());
        var student = userService.findByStudentCode(gradeReview.getStudent().getStudentId());
        var teachers = courseService.getTeachers(gradeReview.getGrade().getAssigment().getCourse().getId());
        var message = new MessageBody(res.getBody());
        for (UserResponse teacher : teachers) {
            if (!Objects.equals(user.getId(), teacher.getId())) {
                wsMessageController.sendApproval(teacher.getId(), message);
            }
        }
        wsMessageController.sendApproval(student.getId(), message);
        return res;
    }


    // TODO TEsting
    @GetMapping("/comments")
    public ResponseEntity<?> getGradeReviewComments(@RequestParam("CourseId") Integer courseId, @RequestParam("GradeId") Integer gradeId, @RequestParam("GradeReviewId") Integer gradeReviewId, @RequestParam("CurrentUser") String currentUser) {
        if (!validateGradeReviewOfUser(currentUser, courseId, ERole.None, false, gradeId, gradeReviewId)) {
            return ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).content("").message("Permission denied for getting grade review comments").build());
        }

        var comments = gradeReviewService.findAllCommentsByGradeReview(gradeReviewId);
        var response = comments.stream().map(ReviewCommentResponse::new).toList();
        var wrapper = new HashMap<String, Object>();
        wrapper.put("data", response);
        wrapper.put("hasMore", false);
        return ok(CommonResponse.builder().status(EStatus.Success)
                .result(EResult.Successful)
                .content(wrapper).message("Get comment for grade review successfully").build());
    }


    // TODO TESTING
    @PutMapping("/update")
    public ResponseEntity<?> putUpdateGradeReview(HttpServletRequest httpServletRequest,
                                                  @RequestBody UpdateGradeReviewRequest request) {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Teacher, false,
                request.getGradeId(), request.getGradeReviewId())) {
            return ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).content("").message("Permission denied for updating grade review").build());
        }
        var gradeReview = gradeReviewService.findGradeReviewByIdAndCreateByAndStatus(request.getGradeReviewId(), user.getUserName(), EGradeReviewStatus.Pending.getValue());
        if (gradeReview == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Update grade review failed !!")
                    .build());
        }
        gradeReview.setGradeExpect(request.getGradeExpect());
        gradeReview.setMessage(request.getReason());
        var updatedGradeReview = gradeReviewService.update(gradeReview, currentUser);
        var response = new GradeReviewResponse(updatedGradeReview);
        var student = gradeReview.getStudent();
        response.setGrade(new GradeResponse(updatedGradeReview.getGrade()));
        response.setExerciseName(updatedGradeReview.getGrade().getAssigment().getName());
        response.setStudent(new StudentResponse(student));
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(response)
                .message("Update grade review successfully")
                .build());
    }

    // TODO Testing
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponse<Serializable>> deleteGradeReview(HttpServletRequest httpRequest,
                                                                          @RequestBody DeleteGradeReviewRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        var badRequest = ok(CommonResponse.builder()
                .status(EStatus.Error)
                .result(EResult.Error)
                .content("")
                .message("Delete grade review failed !!")
                .build());
        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Student, true, request.getGradeId(), request.getGradeReviewId())) {
            return badRequest;
        }
        var gradeReview = gradeReviewService.findGradeReviewByIdAndCreateByAndStatus(request.getGradeReviewId(), currentUser, EGradeReviewStatus.Pending.getValue());
        if (gradeReview == null) {
            return badRequest;
        }
        gradeReviewService.delete(gradeReview);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(request.getGradeReviewId())
                .message("Delete grade review successfully")
                .build());
    }

    // TODO TESTING
    @PostMapping("/teacher-comment")
    public ResponseEntity<CommonResponse<?>> postTeacherComment(@RequestBody CreateTeacherCommentRequest request, HttpServletRequest httpServletRequest) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Teacher, false, request.getGradeId(), request.getGradeReviewId())) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for teacher create comment!!")
                    .build());
        }
        var reviewComment = gradeReviewService.createReviewComment(request.getGradeReviewId(), request.getMessage(), user.getId(), 0, currentUser);
        var gradeReview = reviewComment.getGradeReview();
        Grade grade = reviewComment.getGradeReview().getGrade();
        var assignment = grade.getAssigment();
        var student = grade.getStudent();
        notificationService.createStudentNotification(CreateStudentNotificationSingleArgs.builder()
                .gradeReviewId(gradeReview.getId())
                .courseId(request.getCourseId())
                .gradeId(grade.getId())
                .studentId(student.getStudentId())
                .message(String.format("%s bình luận trong yêu cầu sửa điểm của bài tập tên: %s", user.getNormalizedDisplayName(), assignment.getName()))
                .build());
        var response = new ReviewCommentResponse(reviewComment);
        response.setTeacher(new UserResponse(user));
        User studentUser = userService.findByStudentCode(student.getStudentId());
        var res = CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(response)
                .message("Create new comment review successfully")
                .build();

        var messageBody = new MessageBody(response);
        wsMessageController.addComment(studentUser.getId(), messageBody);
        log.info("Socket5 {}", studentUser.getId());
        var teachers = courseService.getTeacherIds(request.getCourseId());
        for (Integer teacherId : teachers) {
            if (!Objects.equals(teacherId, user.getId())) {
                wsMessageController.addComment(teacherId, messageBody);
            }
        }

        return ok(res);
    }

    // TODO TESTING
    @PutMapping("/teacher-comment/update")
    public ResponseEntity<?> putTeacherUpdateComment(HttpServletRequest httpServletRequest,
                                                     @RequestBody UpdateCommentRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();

        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Teacher, false, request.getGradeId(), request.getGradeReviewId())) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for teacher update comment!!")
                    .build());
        }

        var reviewComment = gradeReviewService.findReviewCommentById(request.getReviewCommentId());
        if (reviewComment == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for teacher update comment because of not found reviewCommnet id " + request.getReviewCommentId())
                    .build());
        }
        reviewComment.setMessage(request.getMessage());
        var updatedComment = gradeReviewService.update(reviewComment, currentUser);
        var response = new ReviewCommentResponse(updatedComment);
        var res = ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(response)
                .message("Update comment success")
                .build());
        var studentId = userService.findByStudentCode(reviewComment.getGradeReview().getStudent().getStudentId()).getId();
        var body = new MessageBody(res.getBody());
        wsMessageController.updateComment(studentId, body);
        var teachers = courseService.getTeacherIds(request.getCourseId());
        for (Integer teacherId : teachers) {
            if (!Objects.equals(teacherId, user.getId())) {
                wsMessageController.updateComment(teacherId, body);
            }
        }

        return res;
    }

    // TODO TESTING
    @DeleteMapping("/teacher-comment/delete")
    public ResponseEntity<?> deleteTeacherTeacherComment(HttpServletRequest httpServletRequest,
                                                         @RequestBody DeleteTeacherCommentRequest request) {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        if (!validateGradeReviewOfUser(currentUser, request.getCourseId(), ERole.Teacher, false, request.getGradeId(), request.getGradeReviewId())) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for teacher update comment!!")
                    .build());
        }

        var reviewComment = gradeReviewService.findReviewCommentById(request.getReviewCommentId());
        if (reviewComment == null || !Objects.equals(reviewComment.getCreateBy(), currentUser)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for teacher update comment because of not found grade review comment id " + request.getGradeReviewId())
                    .build());
        }

        gradeReviewService.delete(reviewComment);
        var res = ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(request.getReviewCommentId())
                .message("Delete comment review successfully")
                .build());
        Student student = reviewComment.getGradeReview().getStudent();
        var studentId = userService.findByStudentCode(student.getStudentId()).getId();
        var message = new MessageBody(res.getBody());
        wsMessageController.deleteComment(studentId, message);
        var teachers = courseService.getTeacherIds(request.getCourseId());
        for (Integer teacherId : teachers) {
            if (!Objects.equals(teacherId, user.getId())) {
                wsMessageController.deleteComment(teacherId, message);
            }
        }
        return res;
    }

    // TODO Testing
    @PostMapping("/student-comment")
    public ResponseEntity<?> postStudentComment(@RequestBody CreateStudentCommentRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (!validateGradeReviewOfUser(currentUser, request, ERole.Student, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for student create comment!!")
                    .build());
        }
        var student = studentRepository.findByStudentId(user.getStudentID());
        ReviewComment reviewComment = gradeReviewService.createReviewComment(request.getGradeReviewId(), request.getMessage(), 0, student.getId(), currentUser);
        var gradeReview = reviewComment.getGradeReview();
        var grade = gradeReview.getGrade();
        var assignment = grade.getAssigment();
        var msg = String.format("%s bình luận grade review cho assignment %s", student.getFullName(), assignment.getName());
        notificationService.createRequestGradeReviewNotification(currentUser, gradeReview, msg, student);
        var response = new ReviewCommentResponse(reviewComment);
        var teachers = courseService.getTeacherIds(request.getCourseId());
        var res = CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(response)
                .message("Create new comment successfully")
                .build();

        var message = new MessageBody(response);
        for (Integer teacherId : teachers) {
            wsMessageController.addComment(teacherId, message);
        }

        return ok(res);
    }

    // TODO Testing
    @PutMapping("/student-comment/update")
    public ResponseEntity<?> putStudentUpdateComment(@RequestBody StudentUpdateCommentRequest request,
                                                     HttpServletRequest httpServletRequest) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (!validateGradeReviewOfUser(currentUser, request, ERole.Student, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for student update comment!!")
                    .build());
        }
        var reviewComment = gradeReviewService.findReviewCommentById(request.getReviewCommentId());
        if (reviewComment == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for student update comment because of not found review comment for id " + request.getReviewCommentId())
                    .build());
        }
        reviewComment.setMessage(request.getMessage());
        var updatedComment = gradeReviewService.update(reviewComment, currentUser);
        var response = new ReviewCommentResponse(updatedComment);
        var res = ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(response)
                .message("Update comment review successfully")
                .build());
        var teachers = courseService.getTeacherIds(request.getCourseId());
        var message = new MessageBody(res.getBody());
        for (Integer teacherId : teachers) {
            if (!Objects.equals(teacherId, user.getId())) {
                wsMessageController.updateComment(teacherId, message);
            }
        }
        return res;
    }

    // TODO Testing
    @DeleteMapping("/student-comment/delete")
    public ResponseEntity<?> deleteStudentDeleteComment(HttpServletRequest httpServletRequest,
                                                        @RequestBody DeleteStudentCommentRequest request) {
        var user = AuthenticationUtils.appUserDetails().unwrap();
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        if (!validateGradeReviewOfUser(currentUser, request, ERole.Student, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for student delete comment!!")
                    .build());
        }
        var reviewComment = gradeReviewService.findReviewCommentById(request.getReviewCommentId());
        if (reviewComment == null || !Objects.equals(reviewComment.getCreateBy(), currentUser)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Not found review comment by id !!" + request.getReviewCommentId())
                    .build());
        }
        gradeReviewService.delete(reviewComment);
        var res = ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(request.getReviewCommentId())
                .message("Delete review comment successfully")
                .build());
        var teachers = courseService.getTeacherIds(request.getCourseId());
        var socketResponse = new MessageBody(res.getBody());
        for (Integer teacherId : teachers) {
            if (!Objects.equals(teacherId, user.getId())) {
                wsMessageController.deleteComment(teacherId, socketResponse);
            }
        }
        return res;
    }
}
