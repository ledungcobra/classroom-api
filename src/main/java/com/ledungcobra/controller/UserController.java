package com.ledungcobra.controller;

import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.dto.user.changePassword.ChangePasswordRequest;
import com.ledungcobra.dto.user.getProfile.GetProfileRequest;
import com.ledungcobra.dto.user.getUserByStudentCode.UserSimpleResponse;
import com.ledungcobra.dto.user.login.LoginRequest;
import com.ledungcobra.dto.user.login.LoginResponse;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.dto.user.update.UpdateProfileRequest;
import com.ledungcobra.user.entity.User;
import com.ledungcobra.user.service.UserService;
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
import java.util.stream.Collectors;

import static com.ledungcobra.common.Constants.ADMIN_ROLE;
import static com.ledungcobra.common.Constants.USER_ROLE;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserController {


    public static final String ACCOUNT_ALREADY_TAKEN_MSG = "Tài khoản đã có người đăng kí";
    public static final String LOGIN_SUCCESS_MSG = "Đăng nhập thành công";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerDto) {

        var exist = userService.checkExists(registerDto.getUsername());
        if (exist) {
            return ResponseEntity.badRequest().body(SingleResponse.builder()
                    .result(ACCOUNT_ALREADY_TAKEN_MSG)
                    .build());
        } else {
            var user = userService.register(registerDto);

            // :TODO Implement mail service
            var resp = SingleResponse.builder()
                    .result(new UserResponse(user))
                    .build();

            return ResponseEntity.created(URI.create("/api/users/registers/"))
                    .body(resp);
        }
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    CommonResponse.builder()
                            .status(EApiStatus.Error)
                            .result(EResponseResult.Error)
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
                default -> throw new Exception("Not implemented");
            };

            return ResponseEntity.ok(CommonResponse.builder()
                    .result(EResponseResult.Successful)
                    .status(EApiStatus.Success)
                    .content(LoginResponse.builder()
                            .email(user.getEmail())
                            .id(user.getId())
                            .fullName(user.getNormalizedDisplayName())
                            .token(jwtUtils.generateToken(appUserDetails))
                            .refreshToken("")
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
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .message("Bearer token is required")
                    .build());
        }
        token = token.split(" ")[1];
        var username = jwtUtils.getUserNameFromJwtToken(token);
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!username.equals(request.getCurrentUser())) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.builder()
                            .message("Bạn không thể đổi thông tin của người dùng khác")
                            .status(EApiStatus.Error)
                            .result(EResponseResult.Error)
                            .content("")
                            .build());
        }

        var user = userService.findByUsername(request.getCurrentUser());
        if (user == null) {
            return new ResponseEntity<>(CommonResponse.<String>builder()
                    .status(EApiStatus.Error)
                    .result(EResponseResult.Error)
                    .content("")
                    .message("Không tìm được user")
                    .build(), HttpStatus.NOT_FOUND);
        }
        User updated = userService.updateProfile(user, request);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
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
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .message("Mật khẩu không khớp")
                    .content("")
                    .result(EResponseResult.Error)
                    .status(EApiStatus.Error)
                    .build());
        }

        if (!StringUtils.hasText(request.getNewPassword()) ||
                request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .status(EApiStatus.Error)
                    .result(EResponseResult.Error)
                    .content("")
                    .message("Mật khẩu không hợp lệ")
                    .build());
        }

        var hashedNewPassword = passwordEncoder.encode(request.getNewPassword());
        User user = userDetails.unwrap();
        user.setPasswordHash(hashedNewPassword);
        userService.update(user);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
                .message("Cập nhật mật khẩu thành công")
                .content("")
                .build());
    }

    @Secured({USER_ROLE, ADMIN_ROLE})
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestBody GetProfileRequest request) {

        User user = userService.findByUsername(request.getUsername());

        if (user == null) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .message("Không tìm được user")
                    .content("")
                    .result(EResponseResult.Error)
                    .status(EApiStatus.Error)
                    .build(), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(CommonResponse.builder()
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
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
                    .status(EApiStatus.Error)
                    .result(EResponseResult.Error)
                    .message(String.format("Không tìm được học sinh với id: %s", studentCode))
                    .build(), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(CommonResponse.builder()
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
                .content(new UserSimpleResponse(foundStudent))
                .build());
    }


}
