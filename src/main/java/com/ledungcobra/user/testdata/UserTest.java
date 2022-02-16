package com.ledungcobra.user.testdata;


import com.ledungcobra.dto.user.login.LoginRequest;
import com.ledungcobra.dto.user.register.RegisterUserDto;

public class UserTest {
    public static final RegisterUserDto registerUserDto_Success = RegisterUserDto.builder()
            .username("test2")
            .password("test897879812738")
            .email("ledungcobra@gmail.com")
            .firstName("dung")
            .lastName("le")
            .middleName("quoc")
            .phoneNumber("0909909999")
            .build();

    public static final RegisterUserDto registerUserDto_Fail = RegisterUserDto.builder()
            .username("test")
            .password("test897879812738")
            .email("ledungcobra@gmail.com")
            .firstName("dung")
            .lastName("le")
            .middleName("quoc")
            .phoneNumber("0909909999")
            .build();

    public static final LoginRequest loginRequest_Success = LoginRequest.builder()
            .username("tanhank2k")
            .password("Abcd1234")
            .build();

    public static final String TEST_EMAIL = "tanhanh2kocean@gmail.com";

}
