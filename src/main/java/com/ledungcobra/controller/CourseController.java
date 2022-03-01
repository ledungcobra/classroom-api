package com.ledungcobra.controller;

import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.websocket.WsNotificationController;
import com.ledungcobra.course.entity.*;
import com.ledungcobra.course.repository.AssignmentRepository;
import com.ledungcobra.course.repository.CourseStudentRepository;
import com.ledungcobra.course.repository.CourseUserRepository;
import com.ledungcobra.course.repository.StudentRepository;
import com.ledungcobra.course.service.CourseService;
import com.ledungcobra.course.service.NotificationService;
import com.ledungcobra.dto.common.*;
import com.ledungcobra.dto.course.addStudentIntoCourseByLink.AddMemberIntoCourseByLinkRequest;
import com.ledungcobra.dto.course.createNewAssignment.CreateNewAssignmentArgs;
import com.ledungcobra.dto.course.createNewAssignment.CreateNewAssignmentsRequest;
import com.ledungcobra.dto.course.getAllGrades.GradeResponseWrapper;
import com.ledungcobra.dto.course.getAllGrades.GradeSimpleResponse;
import com.ledungcobra.dto.course.getAssignmentsOfCourse.AssignmentResponse;
import com.ledungcobra.dto.course.getAssignmentsOfCourse.AssignmentWrapper;
import com.ledungcobra.dto.course.getCourseById.CourseResponse;
import com.ledungcobra.dto.course.getCourseById.CourseWrapperResponse;
import com.ledungcobra.dto.course.getGradeByStudent.SingleStudentGradeResponse;
import com.ledungcobra.dto.course.getMembersInCourse.MemberCourseResponse;
import com.ledungcobra.dto.course.index.CourseListWrapper;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.course.postSortAssignment.SortAssignmentRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeNormalRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeSpecificRequestBase;
import com.ledungcobra.dto.course.postUpdateGradeFromFile.FormDataUpload;
import com.ledungcobra.dto.course.removeMember.RemoveMemberInCourseRequest;
import com.ledungcobra.dto.course.updateAssigment.UpdateAssignmentsRequest;
import com.ledungcobra.dto.course.updateRoleMember.UpdateRoleMemberInCourseRequest;
import com.ledungcobra.dto.email.postSendMail.SendMailJoinToCourseRequest;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.mail.EmailService;
import com.ledungcobra.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("/course")
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class CourseController {

    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String NOT_FOUND_COURSE_MSG = "Not found course";
    private final JwtUtils jwtUtils;
    private final CourseService courseService;
    private final UserService userService;
    private final AssignmentRepository assignmentRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final CourseUserRepository courseUserRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final WsNotificationController wsNotificationController;


    @Value("${spring.client-url}")
    private String URL_CLIENT;

    @Secured(Constants.USER_ROLE)
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<?> getCourseById(@RequestParam(value = "Title", required = false) String title, @RequestParam(value = "StartAt", required = false) Integer startAt, @RequestParam(value = "MaxResults", required = false) Integer maxResults, @RequestParam(value = "SortColumn", required = false) String sortColumn, HttpServletRequest request) {

        var username = jwtUtils.getUserNameFromRequest(request);
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        title = title == null ? "%%" : "%" + title + "%";
        var count = courseService.countByOwner(username);
        var courses = courseService.getCoursesByTitleLike(title, PageableBuilder.getPageable(startAt, maxResults, sortColumn), username);
        var index = (long) startAt + courses.size();
        return ok().body(CommonResponse.builder().content(CourseListWrapper.builder().hasMore(index < count).data(courses.stream().map(CourseResponse::new).toList()).total(count).build()).result(EResult.Successful).status(EStatus.Success).message("Lấy dữ liệu khoá học thành công").build());
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<?> postCreateCourse(HttpServletRequest httpRequest, @RequestBody CreateCourseRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (currentUser == null) {
            return getNotFoundCurrentUser();
        }
        request.setCurrentUser(currentUser);
        var createdCourse = courseService.createCourse(request);
        return ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).content(new CourseResponse(createdCourse)).message("Tạo lớp học thành công").build());
    }

    private ResponseEntity<?> getNotFoundCurrentUser() {
        return ResponseEntity.badRequest().body(CommonResponse.builder().result(EResult.Error).status(EStatus.Error).content("").message("Không tìm thấy user").build());
    }

    @Secured(Constants.USER_ROLE)
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getCourseById(HttpServletRequest httpRequest, @PathVariable("id") Integer id) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (currentUser == null) return getNotFoundCurrentUser();
        Course foundCourse = courseService.findCourseById(currentUser, id);
        if (foundCourse == null) {
            return new ResponseEntity<>(
                    CommonResponse.builder().status(EStatus.Error).result(EResult.Error).message("Class not found").content("").build(),
                    HttpStatus.NOT_FOUND);
        }

        CourseUser courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(user.getId(), foundCourse.getId());
        var response = CourseWrapperResponse.builder()
                .course(new CourseResponse(foundCourse))
                .role(courseUser.getRole().getId())
                .build();
        return ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).message("Get course success")
                .content(response)
                .build());
    }

    @Secured(Constants.USER_ROLE)
    @GetMapping(value = "/{id}/assignments", produces = "application/json")
    public ResponseEntity<?> getAssignmentsOfCourse(HttpServletRequest httpRequest, @PathVariable("id") Integer courseId, @RequestParam(value = "Name", required = false) String name, @RequestParam(value = "StartAt", required = false) Integer startAt, @RequestParam(value = "MaxResults", required = false) Integer maxResults, @RequestParam(value = "SortColumn", required = false) String sortColumn) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (!validateUserInClass(currentUser, courseId, ERole.None, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied!!")
                    .build());
        }

        name = name == null ? "%%" : "%" + name + "%";
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        if (currentUser == null) return getNotFoundCurrentUser();
        List<Assignment> assignments = courseService.findAssignmentOfsCourseLikeName(currentUser, courseId, name, PageableBuilder.getPageable(startAt, maxResults, sortColumn));
        var total = courseService.findCourseById(currentUser, courseId).getAssignments().size();
        var index = startAt + assignments.size();
        return ok(CommonResponse.builder().message("Get assigment successfully").status(EStatus.Success).result(EResult.Successful).content(AssignmentWrapper.builder().total(total).hasMore(index < total).data(assignments.stream().map(AssignmentResponse::new).toList()).build()).build());
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping(value = "/{id}/assignments")
    public ResponseEntity<?> createNewAssignment(
            @PathVariable("id") Integer courseId,
            HttpServletRequest httpServletRequest, @RequestBody CreateNewAssignmentsRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);

        if (!validateUserInClass(currentUser, courseId, ERole.Teacher, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Create assignment is not permitted")
                    .build());
        }

        CreateNewAssignmentArgs args = ReflectionUtils.mapFrom(request);
        var assignment = courseService.createAssignment(args);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message("Create new assignment successfully")
                .content(new AssignmentResponse(assignment))
                .build());
    }

    @Secured(Constants.USER_ROLE)
    @PutMapping("/{id}/assignments/{assignmentId}")
    public ResponseEntity<?> updateAssignment(HttpServletRequest httpRequest, @PathVariable("id") Integer courseId, @PathVariable("assignmentId") Integer assignmentId, @RequestBody UpdateAssignmentsRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        request.setCurrentUser(currentUser);

        if (!validateUserInClass(currentUser, courseId, ERole.Teacher, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Update assignment is not permitted")
                    .build());
        }
        try {
            var updatedAssignment = new AssignmentResponse(courseService.updateAssignment(request, request.getCurrentUser(), assignmentId));
            return ok(CommonResponse.builder().result(EResult.Successful).status(EStatus.Success).message("Update assignment successfully").content(updatedAssignment).build());
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).message(String.format("Not found assignment for assignment id: %d", assignmentId)).content("").build(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param username
     * @param courseId
     * @param role         default none
     * @param isCheckOwner default false
     * @return
     */
    private boolean validateUserInClass(String username, int courseId, ERole role, boolean isCheckOwner) {
        var user = userService.findByUsername(username);
        if (user == null) return false;
        Course course = courseService.findCourseById(username, courseId);
        if (course == null) return false;
        var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(user.getId(), courseId);
        if (courseUser == null) {
            return false;
        }
        if (role.getValue() != ERole.None.getValue()) {
            return courseUser.getRole().getId() == role.getValue();
        }
        if (isCheckOwner) {
            return Objects.equals(course.getCreateBy(), username);
        }
        return true;
    }

    private boolean validateUserInClass(String username, int courseId, ERole role) {
        return validateUserInClass(username, courseId, role, false);
    }

    @Secured(Constants.USER_ROLE)
    @DeleteMapping("/{id}/assignments/{assignmentId}")
    public ResponseEntity<?> deleteAssignment(@PathVariable("id") Integer courseId, @PathVariable("assignmentId") Integer assignmentId, HttpServletRequest httpRequest, @RequestParam("CurrentUser") String currentUser) {
        var username = jwtUtils.getUserNameFromRequest(httpRequest);
        if (!validateUserInClass(username, courseId, ERole.Teacher, false)) {
            return ResponseEntity.ok()
                    .body(CommonResponse.builder().result(EResult.Error).status(EStatus.Error)
                            .message("Delete assignment is not permitted").content("").build()
                    );
        }
        try {
            courseService.deleteAssignment(assignmentId);
            return ResponseEntity.ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).message("Delete assigment success").content(assignmentId).build());
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CommonResponse.builder().message(String.format("Not found assignment by id %d", assignmentId)).content("").result(EResult.Error).status(EStatus.Error).build(), HttpStatus.NOT_FOUND);
        }

    }

    @Secured(Constants.USER_ROLE)
    @PostMapping("/{id}/assignments-sort")
    public ResponseEntity<?> postSortAssignment(HttpServletRequest httpRequest, @PathVariable("id") Integer id, @RequestBody SortAssignmentRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (!validateUserInClass(currentUser, id, ERole.None, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for sorting assignments")
                    .build());
        }
        if (request.getAssignmentSimples().size() != 2) {
            return ResponseEntity.badRequest().body(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).message("AssignmentSimples must have the size of 2").content("").build());
        }

        var first = request.getAssignmentSimples().get(0);
        var second = request.getAssignmentSimples().get(1);
        var firstAssignment = courseService.findAssigmentById(id, first.getId());
        var secondAssignment = courseService.findAssigmentById(id, second.getId());

        firstAssignment.setOrder(first.getOrder());
        secondAssignment.setOrder(second.getOrder());
        firstAssignment = assignmentRepository.save(firstAssignment);
        secondAssignment = assignmentRepository.save(secondAssignment);
        return ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).content((Serializable) List.of(new AssignmentResponse(firstAssignment), new AssignmentResponse(secondAssignment))).message("Sort assignment successfully").build());
    }

    @Secured(Constants.USER_ROLE)
    @GetMapping("/{id}/all-grades")
    public ResponseEntity<?> getAllGrades(@PathVariable("id") Integer courseId, @RequestParam("currentUser") String currentUser) {
        if (!validateUserInClass(currentUser, courseId, ERole.Teacher, false)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Grade all grades failed!!")
                    .build());
        }
        Course course = courseService.findCourseById(currentUser, courseId);
        if (course == null) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.builder()
                            .message(String.format("Not found course by id %d", courseId))
                            .content("")
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .build()
                    );
        }

        var assignments = course.getAssignments().stream().map(AssignmentResponse::new).toList();
        var result = courseService.getAllGrades(courseId);
        return ResponseEntity.ok().body(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).message("Get all grades successfully").content(GradeResponseWrapper.builder().header(assignments).scores(result).total(result.size()).build()).build());
    }

    private final StudentRepository studentRepository;

    @Secured(Constants.USER_ROLE)
    @GetMapping("/{id}/all-grades/student")
    public ResponseEntity<?> getGradeByStudent(@PathVariable("id") Integer courseId, HttpServletRequest httpRequest) throws NotFoundException {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (!validateUserInClass(currentUser, courseId, ERole.Student)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for get all grade of student ")
                    .build());
        }

        var user = userService.findByUsername(currentUser);
        if (user == null) return ResponseEntity.notFound().build();
        var student = studentRepository.findByStudentId(user.getStudentID());
        var result = courseService.getGradeOfCourseByStudentTableId(courseId, student.getId());
        Course course = courseService.findCourseById(currentUser, courseId);
        if (course == null) throw new NotFoundException("Not found course by id " + courseId);
        return ResponseEntity.ok().body(CommonResponse.builder().result(EResult.Successful).status(EStatus.Success).message("Get grade for student successfully").content(SingleStudentGradeResponse.builder().total(1).header(course.getAssignments().stream().map(AssignmentResponse::new).toList()).scores(result).build()).build());
    }

    @Secured(Constants.USER_ROLE)
    @GetMapping("/{id}/assignments/{assignmentId}/all-grades")
    public ResponseEntity<?> getAllGradesV2(@PathVariable("id") Integer id, @PathVariable("assignmentId") Integer assignmentId, @RequestParam(value = "currentUser", required = false) String currentUser, HttpServletRequest httpRequest) {
        currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (!validateUserInClass(currentUser, id, ERole.Teacher)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Get all grade assignments failed!!")
                    .build());
        }
        var result = courseService.getAllGradeAssignment(assignmentId);
        return ResponseEntity.ok().body(CommonResponse.builder().content(DataResponse.builder().data(result).total(result.size()).build()).message("Get assignment grade successfully").status(EStatus.Success).result(EResult.Successful).build());
    }


    @SneakyThrows
    @GetMapping(value = "/download-template-update-member", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadTemplateUpdateMember(HttpServletResponse response) {
        var resource = new ByteArrayResource(ExcelHelper.createFileExcel("StudentID", "FullName"));
        var fileName = "TemplateUpdateMember.xlsx";
        response.setHeader(CONTENT_DISPOSITION, String.format("attachment; filename=%s; filename*=UTF-8''%s", fileName, fileName));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength()).body(resource);
    }


    @SneakyThrows
    @GetMapping(value = "/download-template-update-grade", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadTemplateUpdateGrade(HttpServletResponse response) {
        var resource = new ByteArrayResource(ExcelHelper.createFileExcel("StudentID", "Grade"));
        var fileName = "UpdateGradeTemplate.xlsx";
        response.setHeader(CONTENT_DISPOSITION, String.format("attachment; filename=%s;filename*=UTF-8''%s", fileName, fileName));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(resource.contentLength()).body(resource);
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping(value = "/{id}/assignments/{assignmentsId}/update-grade", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postUpdateGradeFromFile(@PathVariable("id") Integer courseId, @PathVariable("assignmentsId") Integer assignmentsId, @ModelAttribute FormDataUpload formDataUpload, HttpServletRequest request) throws IOException, NotFoundException {
        final String currentUser = jwtUtils.getUserNameFromRequest(request);
        if (formDataUpload.getFile() == null) return badRequest().build();
        var assignment = assignmentRepository.findById(assignmentsId).orElseThrow(() -> new NotFoundException("Not found assignment by id " + assignmentsId));

        try (var book = new XSSFWorkbook(formDataUpload.getFile().getInputStream());) {

            XSSFSheet sheet = book.getSheetAt(0);

            boolean goNext = true;
            for (Row row : sheet) {
                if (goNext) {
                    goNext = false;
                    continue;
                }
                var studentIdCell = row.getCell(0);
                var gradeCell = row.getCell(1);
                var studentId = tryParseString(studentIdCell);
                var student = studentRepository.findByStudentId(studentId);
                var g = Grade.builder().assigment(assignment).description("").gradeAssignment((float) gradeCell.getNumericCellValue()).mssv(studentId).isFinalized((byte) 0).student(student).build();
                courseService.saveGrade(g, currentUser, assignmentsId, courseId);
            }
            return ok(CommonResponse.builder().result(EResult.Successful).status(EStatus.Success).content("").message("Update grade successfully").build());
        }
    }

    public String tryParseString(Cell cell) {
        try {
            return cell.getStringCellValue();
        } catch (Exception e) {
            e.printStackTrace();
            return Objects.toString((int) cell.getNumericCellValue());
        }
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping(value = "/{id}/update-student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postUpdateStudentList(@PathVariable("id") Integer courseId,
                                                   @ModelAttribute FormDataUpload formDataUpload,
                                                   HttpServletRequest request) throws IOException {
        var currentUser = jwtUtils.getUserNameFromRequest(request);
        if (!validateUserInClass(currentUser, courseId, ERole.None, true)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Update student failed because of permission denied!!")
                    .build());
        }
        var studentList = convertFileToListStudents(new XSSFWorkbook(formDataUpload.getFile().getInputStream()));
        courseService.updateStudentsInCourse(studentList, courseId, currentUser);
        return ResponseEntity.ok().body(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).content("").message("Update members in class success").build());
    }

    private List<Student> convertFileToListStudents(XSSFWorkbook book) throws IOException {
        try {
            var sheet = book.getSheetAt(0);
            var list = new ArrayList<Student>();
            var next = true;
            for (Row row : sheet) {
                if (next) {
                    next = false;
                    continue;
                }
                list.add(Student.builder().dateOfBirth(Instant.now()).studentId(tryParseString(row.getCell(0))).fullName(row.getCell(1).getStringCellValue()).build());
            }
            return list;
        } finally {
            book.close();
        }

    }

    @Secured(Constants.USER_ROLE)
    @PostMapping("/{id}/assignments/{assignmentsId}/update-grade-normal")
    public ResponseEntity<?> postUpdateAssignmentNormal(@PathVariable("id") Integer courseId, @PathVariable("assignmentsId") Integer assignmentsId, @RequestBody UpdateGradeNormalRequest request, HttpServletRequest httpServletRequest) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        request.setCurrentUser(currentUser);
        if (!validateUserInClass(request.getCurrentUser(), courseId, ERole.Teacher)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for update grade for all ")
                    .build());
        }
        var success = courseService.updateGradeNormal(request, assignmentsId, courseId);
        if (success) {
            var user = userService.findByUsername(request.getCurrentUser());
            var studentIds = request.getScores().stream().map(UpdateGradeSpecificRequestBase::getMssv).toList();
            var students = studentIds.stream().map(studentRepository::findByStudentId).toList();
            var assignment = courseService.findAssigmentById(courseId, assignmentsId);
            var notifications = notificationService.createStudentNotifications(CreateStudentNotificationsArgs.builder().courseId(courseId).students(students).assignmentId(assignmentsId).message(String.format("%s đã trả điểm cho bài tập %s", user.getNormalizedUserName(), assignment.getName())).currentUser(request.getCurrentUser()).build());
            wsNotificationController.sendNotification(notifications);
        }
        return ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).content(success).message("Update grade normal success").build());
    }


    @Secured(Constants.USER_ROLE)
    @PostMapping("/{id}/assignments/{assignmentsId}/update-grade-finalized")
    public ResponseEntity<?> postUpdateGradeFinalized(@PathVariable("id") Integer courseId, @PathVariable("assignmentsId") Integer assignmentsId, @RequestBody UpdateGradeSpecificRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        request.setCurrentUser(jwtUtils.getUserNameFromRequest(httpServletRequest));
        if (!validateUserInClass(request.getCurrentUser(), courseId, ERole.Teacher)) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Permission denied for update grade for specific ")
                    .build());
        }
        var result = courseService.updateGradeSpecific(UpdateGradeSpecificArgs.builder().courseId(courseId).assignmentId(assignmentsId).isFinalized(request.getIsFinalized()).gradeAssignment(request.getGrade()).mssv(request.getMssv()).currentUser(request.getCurrentUser()).build());
        var studentUser = userService.findByStudentCode(request.getMssv());
        if (result && studentUser != null) {
            var student = studentRepository.findByStudentId(request.getMssv());
            var user = AuthenticationUtils.appUserDetails().unwrap();
            var assignment = assignmentRepository.findById(assignmentsId).orElseThrow(() -> new NotFoundException("Not found assignment by id " + assignmentsId));
            var notification = notificationService.createStudentNotification(CreateStudentNotificationSingleArgs.builder().courseId(courseId).studentId(student.getStudentId()).gradeId(courseService.findGradeByCourseIdAndAssignmentId(request.getMssv(), courseId, assignmentsId)).message(String.format("%s đã trả điểm cho bài tập %s", user.getNormalizedUserName(), assignment.getName())).currentUser(request.getCurrentUser()).build());
            wsNotificationController.sendNotification(notification);
        }
        return ok(CommonResponse.builder().message("Get update grade for individual successfully").status(EStatus.Success).result(EResult.Successful).content(result).build());
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping(value = "/send-mail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postSendMail(@RequestBody SendMailJoinToCourseRequest request) {
        var tokenClassCode = StringHelper.generateHashString(request.getClassCode());
        var tokenEmail = StringHelper.generateHashString(request.getMailPersonReceive());
        var inviteLink = String.format("%s/class-join?classToken=%s&role=%s&email=%s", URL_CLIENT, tokenClassCode, request.getRole(), tokenEmail);
        emailService.sendMail(request.getMailPersonReceive(), "Mời vào lớp", "Vào lớp", inviteLink, "Email này dùng để mời thành viên vào lớp");
        return ok(CommonResponse.builder().status(EStatus.Success).result(EResult.Successful).content("").message(String.format("Send mail to %s successfully", request.getMailPersonReceive())).build());
    }

    @Secured(Constants.USER_ROLE)
    @GetMapping("/{id}/everyone")
    public ResponseEntity<?> getMembersInCourse(@PathVariable("id") Integer courseId, HttpServletRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(request);
        var listTeachers = courseService.getTeachers(courseId);
        var listStudent = courseService.getStudents(courseId);
        var course = courseService.findCourseById(currentUser, courseId);
        if (course == null) return notFound().build();
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message("Get members in course successfully")
                .content(MemberCourseResponse.builder()
                        .total(listStudent.size() + listTeachers.size())
                        .owner(course.getCreateBy())
                        .teachers(listTeachers)
                        .students(listStudent)
                        .build())
                .build());
    }


    @GetMapping("/{id}/download-grade-board")
    public ResponseEntity<?> getGradeBoard(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        try (var book = new XSSFWorkbook();) {
            XSSFSheet sheet = book.createSheet("GradeBoard");
            var result = courseService.findAllGradeOfCourse(id);
            var assignments = assignmentRepository.findByCourseId(id);
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Họ và Tên");
            row0.createCell(1).setCellValue("MSSV");
            var index = 2;
            for (Assignment assignment : assignments) {
                row0.createCell(index++).setCellValue(assignment.getName());
            }
            for (int i = 0; i < result.size(); i++) {
                var row = sheet.createRow(i + 1);
                var currentGradeOfCourse = result.get(i);

                row.createCell(0).setCellValue(currentGradeOfCourse.getName());
                row.createCell(1).setCellValue(currentGradeOfCourse.getMssv());
                index = 2;

                for (Assignment assignment : assignments) {
                    GradeSimpleResponse foundGrade = currentGradeOfCourse.getGrades().stream().filter(g -> Objects.equals(g.getId(), assignment.getId())).findFirst().orElse(null);
                    if (foundGrade == null) continue;
                    row.createCell(index++).setCellValue(String.format("%s/%s", foundGrade.getGrade().toString(), foundGrade.getMaxGrade().toString()));
                }
            }
            var bos = new ByteArrayOutputStream();
            book.write(bos);
            var byteResource = new ByteArrayResource(bos.toByteArray());
            var fileName = "GradeBoard.xlsx";
            response.setHeader(CONTENT_DISPOSITION, String.format("attachment; filename=%s;filename*=UTF-8''%s", fileName, fileName));
            return ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(byteResource.contentLength()).body(byteResource);
        }
    }

    // TODO testing
    @Secured(Constants.USER_ROLE)
    @PostMapping("/add-member/invite-link")
    public ResponseEntity<?> addStudentIntoCourseByLink(@RequestBody AddMemberIntoCourseByLinkRequest request, HttpServletRequest httpServletRequest) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        request.setCurrentUser(currentUser);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseService.findAllCourses().stream().filter(c -> StringUtils.hasText(c.getCourseCode()) && StringHelper.check(request.getToken(), c.getCourseCode())).findFirst().orElse(null);
        if (course == null) {
            return ok(CommonResponse.builder().status(EStatus.Error).result(EResult.Error).content("").message(NOT_FOUND_COURSE_MSG).build());
        }

        var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(user.getId(), course.getId());

        if (courseUser != null) {
            return ok(CommonResponse.builder()
                    .content("")
                    .result(EResult.Error)
                    .status(EStatus.Error)
                    .message("Already join class")
                    .build());
        }

        if (!StringUtils.hasText(request.getInvitee())) {
            if (courseService.addMemberIntoCourse(user, request.getRole(), course.getId())) {
                return ok(CommonResponse.builder().status(EStatus.Success)
                        .result(EResult.Successful).content("").message("Add member successfully").build());
            } else {
                return ok(CommonResponse.builder()
                        .status(EStatus.Error)
                        .result(EResult.Error)
                        .content("")
                        .message("Add member failed")
                        .build());
            }
        }
        var invitee = userService.findByUsername(request.getInvitee());
        if (invitee == null) {
            return ok(
                    CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content("")
                            .message("Not found invitee user")
                            .build()
            );
        }

        if (courseService.addMemberIntoCourse(invitee.getId(), user.getUserName(), request.getRole(), course.getId())) {
            return ok(CommonResponse.builder().status(EStatus.Success)
                    .result(EResult.Successful).content("").message("Add member successfully").build());
        } else {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Add member failed")
                    .build());
        }
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping("/update-role-member")
    public ResponseEntity<?> updateRoleMember(@RequestBody UpdateRoleMemberInCourseRequest request, HttpServletRequest httpServletRequest) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        var user = AuthenticationUtils.appUserDetails().unwrap();
        if (user == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Not found user")
                    .build());
        }
        var course = courseService.findCourseById(currentUser, request.getCourseId());
        if (course == null) {
            return ok(CommonResponse.builder()
                    .message(NOT_FOUND_COURSE_MSG)
                    .content("")
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .build());
        }

        if (!Objects.equals(user.getUserName(), course.getCreateBy())) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .message("You are not the owner of this course")
                    .content("")
                    .build());
        }

        var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(request.getUserId(), request.getCourseId());
        if (courseUser == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Not found user in class")
                    .build());
        }
        courseService.updateRole(courseUser, request.getRole(), currentUser);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content("")
                .message("Update role user success")
                .build());
    }

    @Secured(Constants.USER_ROLE)
    @PostMapping("/remove-member")
    public ResponseEntity<?> removeMember(@RequestBody RemoveMemberInCourseRequest request, HttpServletRequest httpServletRequest) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpServletRequest);
        request.setCurrentUser(currentUser);

        var user = userService.findByUsername(request.getCurrentUser());
        if (user == null) {
            return ok(CommonResponse.builder()
                    .message("Not found user")
                    .content("")
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .build());
        }

        var course = courseService.findCourseById(currentUser, request.getCourseId());
        if (course == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message(NOT_FOUND_COURSE_MSG)
                    .build());
        }

        if (!Objects.equals(course.getCreateBy(), user.getUserName())) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("You are not the owner of the class")
                    .build());
        }

        var courseUser = courseUserRepository.findCourseUserByUserIdAndCourseId(request.getUserId(), request.getCourseId());
        if (courseUser == null) {
            var courseStudent = courseStudentRepository.findByStudentCode(request.getStudentId());
            if (courseStudent == null) {
                return ok(CommonResponse.builder()
                        .status(EStatus.Error)
                        .result(EResult.Error)
                        .content("")
                        .message("Not found user in class")
                        .build());
            } else {
                try {
                    studentRepository.delete(courseStudent.getStudent());
                    return ok(CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content("")
                            .message("Xoá tài khoản thành công")
                            .build());
                } catch (Exception e) {
                    log.error("Cannot delete {}", e.getMessage());
                    return ok(CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content("")
                            .message("Xoá tài khoản thành công")
                            .build());
                }
            }
        }
        try {
            courseUserRepository.delete(courseUser);
            return ok(CommonResponse.builder()
                    .status(EStatus.Success)
                    .result(EResult.Successful)
                    .content("")
                    .message("Delete member from class success")
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Cannot delete user " + e.getMessage())
                    .build());
        }
    }

}
