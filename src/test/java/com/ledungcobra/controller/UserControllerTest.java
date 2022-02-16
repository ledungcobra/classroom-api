package com.ledungcobra.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.dto.user.login.LoginResponse;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static com.ledungcobra.controller.UserController.ACCOUNT_ALREADY_TAKEN_MSG;
import static com.ledungcobra.controller.UserController.LOGIN_SUCCESS_MSG;
import static com.ledungcobra.user.testdata.UserTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@Transactional
class UserControllerTest extends BaseTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    UserService userService;
    private static final String BASE_URL = "/api/users";
    @Value("${spring.security.jwt.expired-in-seconds}")
    private Integer EXPIRED_TIME_IN_SECONDS;

    private String buildUrl(String endpoint) {
        return BASE_URL + endpoint;
    }

    @Test
    void registerShouldSuccess() throws Exception {

        MvcResult result = mockMvc.perform(post(buildUrl("/register")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto_Success))
                )
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();

        var response = ((UserResponse) objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<SingleResponse<UserResponse>>() {
        }).getResult());
        assertThat(response.getUsername()).isEqualTo(registerUserDto_Success.getUsername());

    }


    @Test
    void registerShouldFail() throws Exception {

        MvcResult result = mockMvc.perform(post(buildUrl("/register")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto_Fail))
                )
                .andExpect(status().isBadRequest())
                .andDo(print()).andReturn();

        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).contains(ACCOUNT_ALREADY_TAKEN_MSG);

    }


    @Test
    void loginShouldSuccess() throws Exception {

        MvcResult result = mockMvc.perform(post(buildUrl("/login")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest_Success))
                )
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<CommonResponse<LoginResponse>>() {
        });

        assertThat(response.getStatus()).isEqualTo(EApiStatus.Success);
        assertThat(response.getResult()).isEqualTo(EResponseResult.Successful);
        assertThat(response.getMessage()).isEqualTo(LOGIN_SUCCESS_MSG);
        var body = response.getContent();
        assertThat(body).isNotNull();
        assertThat(body.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(body.getToken()).isNotNull();
        assertThat(body.getToken()).isNotBlank();
        log.info("Token generated token is {}", body.getToken());

    }


    @Test
    void testJwtTokenShouldExpired() throws InterruptedException {
        var token = generateTestToken();
        Thread.sleep(EXPIRED_TIME_IN_SECONDS * 1000 + 10);
        boolean valid = jwtUtils.validateToken(token);
        assertThat(valid).isFalse();
    }

    private String generateTestToken() {
        UserDetails user = userDetailsService.loadUserByUsername("tanhank2k");
        return jwtUtils.generateToken((AppUserDetails) user);
    }

    @Test
    void testJwnTokenShouldValid() throws InterruptedException {
        var token = generateTestToken();
        var isValid = jwtUtils.validateToken(token);
        Thread.sleep(EXPIRED_TIME_IN_SECONDS * 1000 - 500);
        assertThat(isValid).isTrue();
    }

}