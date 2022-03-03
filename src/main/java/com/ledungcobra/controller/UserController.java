package com.ledungcobra.controller;

import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.dto.user.changePassword.ChangePasswordRequest;
import com.ledungcobra.dto.user.changePassword.Result;
import com.ledungcobra.dto.user.getUserByStudentCode.UserSimpleResponse;
import com.ledungcobra.dto.user.login.LoginRequest;
import com.ledungcobra.dto.user.login.LoginResponse;
import com.ledungcobra.dto.user.postRefreshToken.RefreshToken;
import com.ledungcobra.dto.user.postRefreshToken.RefreshTokenResponse;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.dto.user.resetPassword.ResetPasswordRequest;
import com.ledungcobra.dto.user.update.UpdateProfileRequest;
import com.ledungcobra.exception.NotFoundException;
import com.ledungcobra.mail.EmailService;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ledungcobra.common.Constants.ADMIN_ROLE;
import static com.ledungcobra.common.Constants.USER_ROLE;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
@Log4j2
public class UserController {


    public static final String ACCOUNT_ALREADY_TAKEN_MSG = "Tài khoản đã có người đăng kí";
    public static final String LOGIN_SUCCESS_MSG = "Đăng nhập thành công";

    @Value("${server.port}")
    private String serverPort;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    @Value("${spring.client-url}")
    private String clientUrl;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerDto) {

        var exist = userService.checkExists(registerDto.getUsername());
        if (exist) {
            return badRequest().body(SingleResponse.builder()
                    .result(ACCOUNT_ALREADY_TAKEN_MSG)
                    .build());
        } else {
            var user = userService.register(registerDto);
            var token = jwtUtils.generateToken(new AppUserDetails(user));
            var confirmationLink = StringHelper.getConfirmationLink(token, user.getEmail(), serverPort);
            emailService.sendMail(registerDto.getEmail(), "Xác nhận tài khoản", "Xác nhận", confirmationLink, "Nhấn vào link để kích hoạt tài khoản");
            var resp = SingleResponse.builder()
                    .result(new UserResponse(user))
                    .build();
            return created(URI.create("/users/registers/"))
                    .body(resp);
        }
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return badRequest().body(
                    CommonResponse.builder()
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content(String.join(",",
                                    bindingResult.getAllErrors().stream().map(ObjectError::toString)
                                            .collect(Collectors.toSet())))
                            .build()
            );
        } else {
            var user = userService.findByUsername(loginRequest.getUsername());
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            var appUserDetails = switch (authenticate.getPrincipal()) {
                case AppUserDetails userDetails -> userDetails;
                default -> throw new NotFoundException("Login failt");
            };

            return ok(CommonResponse.builder()
                    .result(EResult.Successful)
                    .status(EStatus.Success)
                    .content(LoginResponse.builder()
                            .email(user.getEmail())
                            .id(user.getId())
                            .fullName(user.getNormalizedDisplayName())
                            .token(jwtUtils.generateToken(appUserDetails))
                            .username(user.getUserName())
                            .refreshToken(jwtUtils.generateRefreshToken(appUserDetails))
                            .build())
                    .message(LOGIN_SUCCESS_MSG)
                    .build());
        }
    }

    /**
     * @param request     payload that user upload to process update profile procedure
     * @param httpRequest used to get Authorization token e.g Bearer <token>
     * @return
     */
    @Secured({USER_ROLE, ADMIN_ROLE})
    @PostMapping(value = "/update", produces = "application/json")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request, HttpServletRequest httpRequest) {

        var token = httpRequest.getHeader("Authorization");
        if (token == null || !token.contains(" ") || !token.contains("Bearer")) {
            return badRequest().body(CommonResponse.builder()
                    .message("Bearer token is required")
                    .build());
        }
        token = token.split(" ")[1];
        var username = jwtUtils.getUserNameFromJwtToken(token);
        if (username == null) {
            return badRequest().build();
        }

        if (!Objects.equals(username, request.getCurrentUser())) {
            return badRequest()
                    .body(CommonResponse.builder()
                            .message("Bạn không thể đổi thông tin của người dùng khác")
                            .status(EStatus.Error)
                            .result(EResult.Error)
                            .content("")
                            .build());
        }

        var user = userService.findByUsername(request.getCurrentUser());
        if (user == null) {
            return new ResponseEntity<>(CommonResponse.<String>builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Không tìm được user")
                    .build(), HttpStatus.NOT_FOUND);
        }
        User updated = userService.updateProfile(user, request);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(new UserResponse(updated))
                .message("Cập nhật thông tin thành công")
                .build());
    }


    @Secured({ADMIN_ROLE, USER_ROLE})
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) {

        AppUserDetails userDetails = AuthenticationUtils.appUserDetails();
        var plainOldPassword = request.getCurrentPassword();
        var hashedOldPassword = userDetails.getPassword();

        if (!passwordEncoder.matches(plainOldPassword, hashedOldPassword)) {
            return badRequest().body(CommonResponse.builder()
                    .message("Mật khẩu không khớp")
                    .content("")
                    .result(EResult.Error)
                    .status(EStatus.Error)
                    .build());
        }

        if (!StringUtils.hasText(request.getNewPassword()) ||
                request.getNewPassword().length() < 8) {
            return badRequest().body(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Mật khẩu không hợp lệ")
                    .build());
        }

        var hashedNewPassword = passwordEncoder.encode(request.getNewPassword());
        User user = userDetails.unwrap();
        user.setPasswordHash(hashedNewPassword);
        userService.update(user);
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .message("Cập nhật mật khẩu thành công")
                .content(new Result(true))
                .build());
    }

    @Secured({USER_ROLE, ADMIN_ROLE})
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam("username") String username) {

        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .message("Không tìm được user")
                    .content("")
                    .result(EResult.Error)
                    .status(EStatus.Error)
                    .build(), HttpStatus.NOT_FOUND);
        }

        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(new UserResponse(user))
                .message("")
                .build());
    }

    @Secured({USER_ROLE, ADMIN_ROLE})
    @GetMapping("/student-code")
    public ResponseEntity<?> getUserByStudentCode(@RequestParam("studentCode") String studentCode) {
        var foundStudent = userService.findByStudentCode(studentCode);
        if (foundStudent == null) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .content("")
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .message(String.format("Không tìm được học sinh với id: %s", studentCode))
                    .build(), HttpStatus.NOT_FOUND);
        }


        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content(new UserSimpleResponse(foundStudent))
                .build());
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        var user = userService.findByEmail(request.getEmail());
        if (user == null) {
            return ok(CommonResponse.builder()
                    .status(EStatus.Error)
                    .result(EResult.Error)
                    .content("")
                    .message("Not found user for reset password")
                    .build());
        }
        var token = jwtUtils.generateToken(new AppUserDetails(user));
        var link = String.format("%s/login?reset-password=true&token=%s&email=%s", clientUrl, token, request.getEmail());
        emailService.sendMail(request.getEmail(), "Khôi phục mật khẩu", "Khôi phục", link, "Nhấn vào đường dẫn bên dưới để khôi phục mật khẩu");
        return ok(CommonResponse.builder()
                .status(EStatus.Success)
                .result(EResult.Successful)
                .content("")
                .message("Mail to reset password success")
                .build());
    }

    @PostMapping("refreshToken")
    public ResponseEntity<?> postRefreshToken(@RequestBody RefreshToken body) {
        log.info("Body {}", body);
        if (jwtUtils.validateToken(body.getRefreshToken())) {
            var currentUser = jwtUtils.getUserNameFromJwtToken(body.getRefreshToken());
            var foundUser = userService.findByUsername(currentUser);
            var userDetails = new AppUserDetails(foundUser);
            if (foundUser != null) {
                var tokenResponse = RefreshTokenResponse.builder()
                        .token(jwtUtils.generateToken(userDetails))
                        .refreshToken(jwtUtils.generateRefreshToken(userDetails))
                        .build();
                return ResponseEntity.ok(CommonResponse.builder()
                        .content(tokenResponse)
                        .status(EStatus.Success)
                        .result(EResult.Successful)
                        .message("")
                        .build());
            } else {
                return new ResponseEntity<>(CommonResponse.builder()
                        .result(EResult.Error)
                        .status(EStatus.Error)
                        .message("Not found")
                        .content("")
                        .build(), HttpStatus.NOT_FOUND);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
