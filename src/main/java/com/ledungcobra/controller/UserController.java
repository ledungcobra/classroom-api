package com.ledungcobra.controller;

import com.ledungcobra.common.EApiStatus;
import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EResponseResult;
import com.ledungcobra.common.SingleResponse;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.dto.user.login.LoginRequest;
import com.ledungcobra.dto.user.login.LoginResponse;
import com.ledungcobra.dto.user.register.RegisterUserDto;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public record UserController(
        UserService userService,
        AuthenticationManager authenticationManager,
        JwtUtils jwtUtils
) {


    public static final String ACCOUNT_ALREADY_TAKEN_MSG = "Tài khoản đã có người đăng kí";
    public static final String LOGIN_SUCCESS_MSG = "Đăng nhập thành công";

    @PostMapping(value = "/register",produces = "application/json")
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

    @PostMapping(value = "/login",produces = "application/json")
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
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),loginRequest.getPassword()));
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

}
