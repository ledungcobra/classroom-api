package com.ledungcobra.controller;

import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.course.service.CourseService;
import com.ledungcobra.dto.admin.createAccount.DataWrapper;
import com.ledungcobra.dto.admin.login.LoginArgs;
import com.ledungcobra.dto.admin.postChangeStudentId.ChangeStudentIDRequest;
import com.ledungcobra.dto.admin.postLockAccount.ApprovalAccountRequest;
import com.ledungcobra.dto.admin.registerAdmin.RegisterNewUserRequest;
import com.ledungcobra.dto.course.getAssignmentsOfCourse.AssignmentResponse;
import com.ledungcobra.dto.course.getCourseById.CourseResponse;
import com.ledungcobra.dto.course.index.CourseListWrapper;
import com.ledungcobra.dto.user.login.LoginResponse;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.exception.StudentIdAlreadyExistException;
import com.ledungcobra.mail.EmailService;
import com.ledungcobra.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static com.ledungcobra.common.Constants.ADMIN_ROLE;
import static com.ledungcobra.controller.UserController.LOGIN_SUCCESS_MSG;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(originPatterns = "*")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final CourseService courseService;

    @Value("${server.port}")
    private String serverPort;

    // Todo testing
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginArgs args) throws NotFoundException {
        var user = userService.findByUsername(args.getUsername());
        if (user.getRole().getId() != ERoleAccount.Admin.getValue()) {
            return new ResponseEntity<>(CommonResponse.builder().build(), HttpStatus.BAD_REQUEST);
        }
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), args.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        var appUserDetails = switch (authenticate.getPrincipal()) {
            case AppUserDetails userDetails -> userDetails;
            default -> throw new NotFoundException("Login fail");
        };

        return ok(CommonResponse.builder().result(EResult.Successful).status(EStatus.Success).content(LoginResponse.builder().email(user.getEmail()).id(user.getId()).role(user.getRole().getId()).fullName(user.getNormalizedDisplayName()).token(jwtUtils.generateToken(appUserDetails)).refreshToken("").build()).message(LOGIN_SUCCESS_MSG).build());
    }

    // TODO Testing
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterNewUserRequest request) {

        var exist = userService.checkExists(request.getUsername());
        if (exist) {
            return ok(CommonResponse.builder().message("User already exist").content("").status(EStatus.Error).result(EResult.Error).build());
        }
        var newUser = userService.register(RegisterUserDto.builder().firstName(request.getFirstName()).middleName(request.getMiddleName()).lastName(request.getLastName()).email(request.getEmail()).phoneNumber(request.getPhoneNumber()).password(request.getPassword()).username(request.getUsername()).imageUrl("").build(), ERoleAccount.Admin);
        var token = jwtUtils.generateToken(new AppUserDetails(newUser));
        var confirmationLink = StringHelper.getConfirmationLink(token, newUser.getEmail(), serverPort);
        emailService.sendMail(newUser.getEmail(), "Kích hoạt tài khoản admin", "Kích hoạt", confirmationLink, "Nhấn vào nút bên dưới để kích hoạt tài khoản");
        return created(URI.create("/admin/register")).body(SingleResponse.builder().result(new UserResponse(newUser)).build());
    }

    @Secured(ADMIN_ROLE)
    @GetMapping("/admin-account")
    public ResponseEntity<?> getAdminAccounts(@RequestParam("Username") String username,
                                              @RequestParam("StartAt") Integer startAt,
                                              @RequestParam("MaxResults") Integer maxResults,
                                              @RequestParam("SortColumn") String sortColumn) {
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        var page = PageableBuilder.getPageable(startAt, maxResults, sortColumn);
        var accounts = userService.findByRoleAndUsername(ERoleAccount.Admin.getValue(), "%" + username + "%", page);
        var total = userService.countByRole(ERoleAccount.Admin.getValue());
        var hasMore = startAt + accounts.size() < total;
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(DataWrapper.builder()
                        .hasMore(hasMore)
                        .total(total)
                        .data(accounts.stream().map(UserResponse::new).toList())
                        .build())
                .content("Get admin accounts success")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @GetMapping("/admin-account/{id}")
    public ResponseEntity<?> getAdminAccountById(@PathVariable("id") Integer id) {
        var adminAccount = userService.findAdminById(id);
        if (adminAccount == null) {
            return badRequest().body(CommonResponse.builder()
                    .status(EStatus.Success)
                    .result(EResult.Successful)
                    .content("")
                    .message("User not found")
                    .build());
        }
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(new UserResponse(adminAccount))
                .message("Get admin success")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @PostMapping("/admin-account/create-account")
    public ResponseEntity<?> createAccount(@RequestBody RegisterNewUserRequest request) {
        var user = userService.register(RegisterUserDto.builder()
                .imageUrl("")
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .build(), ERoleAccount.Admin);

        var token = jwtUtils.generateToken(new AppUserDetails(user));
        var confirmationLink = StringHelper.getConfirmationLink(token, user.getEmail(), serverPort);
        emailService.sendMail(user.getEmail(), "Kích hoạt tài khoản", "Kích hoạt", confirmationLink, "Nhấn vào nút bên dưới để kích hoạt tài khoản admin");
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(new UserResponse(user))
                .message("Create new comment ")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @GetMapping("/user-account")
    public ResponseEntity<?> getUserAccount(@RequestParam(value = "Username", required = false) String username,
                                            @RequestParam(value = "StartAt", required = false) Integer startAt,
                                            @RequestParam("MaxResults") Integer maxResults,
                                            @RequestParam("SortColumn") String sortColumn) {
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        username = username == null ? "" : username;
        var page = PageableBuilder.getPageable(startAt, maxResults, sortColumn);
        var accounts = userService.findByRoleAndUsername(ERoleAccount.User.getValue(), "%" + username + "%", page);
        var total = userService.countByRole(ERoleAccount.User.getValue());
        var hasMore = startAt + accounts.size() < total;
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(DataWrapper.builder()
                        .total(total)
                        .hasMore(hasMore)
                        .data(accounts)
                        .build())
                .content("Get admin accounts success")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @PostMapping("/user-account/change-student-id")
    public ResponseEntity<?> postChangeStudentId(@RequestBody ChangeStudentIDRequest request,
                                                 HttpServletRequest httpServletRequest) throws StudentIdAlreadyExistException {
        String currentUser = jwtUtils.getJwtFromRequest(httpServletRequest);
        var user = userService.findByUsername(currentUser);
        if (user == null || user.getRole().getId() != ERoleAccount.Admin.getValue()) {
            return badRequest().body(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Not found user")
                    .build());
        }

        var userAccount = userService.findByUsername(currentUser);

        if (userAccount == null) {
            return badRequest()
                    .body(CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .message(String.format("Not found account %s", request.getUsername()))
                            .content(String.format("Not found account %s", request.getUsername()))
                            .build());
        }
        if (StringUtils.hasText(request.getMssv())) {
            var student = userService.findByStudentCode(request.getMssv());
            if (student == null) {
                return badRequest().body(CommonResponse.builder()
                        .status(EStatus.Error)
                        .result(EResult.Error)
                        .content("Student not found")
                        .message("Student not found")
                        .build());
            } else {
                student.setStudentID(request.getMssv());
                try {
                    userService.update(student);
                    return ok(CommonResponse.builder()
                            .status(EStatus.Success)
                            .result(EResult.Successful)
                            .message("")
                            .content("")
                            .build());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new StudentIdAlreadyExistException("");
                }
            }
        } else {
            return badRequest().body(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("Student ID is required")
                    .message("Student not is required")
                    .build());
        }
    }

    @Secured(ADMIN_ROLE)
    @PostMapping("/user-account/lock-account")
    public ResponseEntity<?> postLockAccount(@RequestBody ApprovalAccountRequest request) {

        var userAccount = userService.findByUsername(request.getUsername());
        if (userAccount == null) {
            return badRequest()
                    .body(CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content("")
                            .message("Not found account " + request.getUsername())
                            .build());
        }

        userAccount.setUserStatus(EUserStatus.Locked.getValue());
        userService.update(userAccount);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content("")
                .message("Lock account successfully")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @PostMapping("/user-account/unlock-account")
    public ResponseEntity<?> postUnlockAccount(@RequestBody ApprovalAccountRequest request) {
        var userAccount = userService.findByUsername(request.getUsername());
        if (userAccount == null) {
            return badRequest()
                    .body(CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content("")
                            .message("Not found account " + request.getUsername())
                            .build());
        }

        userAccount.setUserStatus(EUserStatus.Active.getValue());
        userService.update(userAccount);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content("")
                .message("Lock account successfully")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @GetMapping("/course")
    public ResponseEntity<?> getCourses(@RequestParam("Title") String title,
                                        @RequestParam("CurrentUser") String currentUser,
                                        @RequestParam("StartAt") Integer startAt,
                                        @RequestParam("MaxResults") Integer maxResults,
                                        @RequestParam("SortColumn") String sortColumn) {
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        var page = PageableBuilder.getPageable(startAt, maxResults, sortColumn);
        var courses = courseService.findCourseByTitle("%" + title + "%", page);
        var courseCount = courseService.countCourse();
        List<CourseResponse> coursesRes = courses.stream().map(CourseResponse::new).toList();
        return ok(CommonResponse.builder()
                .content(CourseListWrapper.builder()
                        .hasMore(startAt + coursesRes.size() < courseCount)
                        .total(courseCount)
                        .data(coursesRes)
                        .build())
                .build());
    }

    @Secured(ADMIN_ROLE)
    @GetMapping("/course/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable("id") Integer courseId) {
        var course = courseService.findCourseById(courseId);
        if (course == null) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Not found course for id " + courseId)
                    .build(), HttpStatus.NOT_FOUND);
        }
        return ok(CommonResponse.builder()
                .result(EResult.Successful)
                .status(EStatus.Success)
                .content(new CourseResponse(course))
                .message("Get course by id successfully")
                .build());
    }

    @Secured(ADMIN_ROLE)
    @GetMapping("/course/assignments")
    public ResponseEntity<?> getAssignments(
            @RequestParam("CourseId") Integer courseId,
            @RequestParam("Name") String name,
            @RequestParam("CurrentUser") String currentUser,
            @RequestParam("StartAt") Integer startAt,
            @RequestParam("MaxResults") Integer maxResults,
            @RequestParam("SortColumn") String sortColumn) {
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        name = name == null ? "" : name;
        var page = PageableBuilder.getPageable(startAt, maxResults, sortColumn);
        var courseAssignments = courseService.getAssignments(courseId, "%" + name + "%", page).stream().map(AssignmentResponse::new).toList();
        var total = courseService.countAssignmentByCourseId(courseId);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message("Get assignments success")
                .content(DataWrapper.builder()
                        .data(courseAssignments)
                        .hasMore(startAt + courseAssignments.size() < total)
                        .total(total)
                        .build())
                .build());
    }

}
