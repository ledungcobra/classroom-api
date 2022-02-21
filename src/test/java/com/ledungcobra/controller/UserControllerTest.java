package com.ledungcobra.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.dto.user.changePassword.ChangePasswordRequest;
import com.ledungcobra.dto.user.getProfile.GetProfileRequest;
import com.ledungcobra.dto.user.getUserByStudentCode.UserSimpleResponse;
import com.ledungcobra.dto.user.login.LoginResponse;
import com.ledungcobra.dto.user.register.UserResponse;
import com.ledungcobra.user.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static com.ledungcobra.controller.UserController.ACCOUNT_ALREADY_TAKEN_MSG;
import static com.ledungcobra.controller.UserController.LOGIN_SUCCESS_MSG;
import static com.ledungcobra.user.testdata.UserTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    private static final String BASE_URL = "/users";
    @Value("${spring.security.jwt.expired-in-seconds}")
    private Integer EXPIRED_TIME_IN_SECONDS;

    private String buildUrl(String endpoint) {
        return BASE_URL + endpoint;
    }

    @Test
    void register_Should_Success() throws Exception {

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
    void register_Should_Fail() throws Exception {

        MvcResult result = mockMvc.perform(post(buildUrl("/register")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto_Fail))
                )
                .andExpect(status().isBadRequest())
                .andDo(print()).andReturn();

        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).contains(ACCOUNT_ALREADY_TAKEN_MSG);

    }


    @Test
    void login_Should_Success() throws Exception {

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
    void testJwtToken_Should_Expired() throws InterruptedException {
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
    void testJwnToken_Should_Valid() throws InterruptedException {
        var token = generateTestToken();
        var isValid = jwtUtils.validateToken(token);
        Thread.sleep(EXPIRED_TIME_IN_SECONDS * 1000 - 500);
        assertThat(isValid).isTrue();
    }

    @Test
    void testRegisterDuplicateEmail_Should_Fail() throws Exception {
        MvcResult result = mockMvc.perform(post(buildUrl("/register")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto_Success))
                )
                .andExpect(status().isCreated())
                .andDo(print()).andReturn();

        var response = ((UserResponse) objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<SingleResponse<UserResponse>>() {
        }).getResult());
        assertThat(response.getUsername()).isEqualTo(registerUserDto_Success.getUsername());

        mockMvc.perform(post(buildUrl("/register")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDto_Success))
                )
                .andExpect(status().is4xxClientError())
                .andDo(print()).andReturn();

    }

    @Test
    void updateProfile_Should_ReturnBadRequest() throws Exception {
        callUpdateProfile(updateProfile_Success, null)
                .andExpect(status().isForbidden())
                .andDo(print()).andReturn();
    }

    final String testUser = "tanhank2k";

    @Test
    void updateProfile_Should_ReturnOk() throws Exception {

        var result = callUpdateProfile(updateProfile_Success, testUser)
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<UserResponse>>() {
        });

        var userUpdated = response.getContent();
        assertThat(userUpdated).isNotNull();
    }

    private ResultActions callUpdateProfile(Object request, String username) throws Exception {
        return mockMvc.perform(post(buildUrl("/update")).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .headers(jwtUtils.buildAuthorizationHeader(username))
        );
    }

    @Test
    void updateProfileForAnotherUser_Should_ReturnBadRequest() throws Exception {
        callUpdateProfile(updateProfile_Success, "dungle")
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserPassword_Should_ReturnOk() throws Exception {
        var newPassword = "testNewPassword";
        mockMvc.perform(
                        post("/users/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(jwtUtils.buildAuthorizationHeader(testUser))
                                .content(objectMapper.writeValueAsString(ChangePasswordRequest.builder()
                                        .newPassword(newPassword)
                                        .currentPassword("Abcd1234")
                                        .currentUser("tanhank2k")
                                        .build()))
                )
                .andDo(print())
                .andExpect(status().isOk());

        var updatedUser = userService.findByUsername("tanhank2k");
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPasswordHash())).isTrue();
    }

    @Test
    void findProfile_Should_Success() throws Exception {
        var user = userService.findByUsername(testUser);
        var mvcResult = mockMvc.perform(get("/users/profile")
                        .headers(jwtUtils.buildAuthorizationHeader(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GetProfileRequest.builder()
                                .username(testUser)
                                .build()))
                ).andExpect(status().isOk())
                .andReturn();
        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<CommonResponse<UserResponse>>() {
        });
        var foundUser = response.getContent();
        assertThat(response).isNotNull();
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(user.getUserName());
        assertThat(foundUser.getId()).isNotZero();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(foundUser.getMiddleName()).isEqualTo(user.getMiddleName());
        assertThat(foundUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(foundUser.getFullname()).isEqualTo(String.format("%s %s %s", foundUser.getFirstName(), foundUser.getMiddleName(), foundUser.getLastName()));
    }

    @Test
    void findProfile_Should_ReturnNotFound() throws Exception {
        mockMvc.perform(get("/users/profile")
                        .headers(jwtUtils.buildAuthorizationHeader(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GetProfileRequest.builder()
                                .username("kalsjk")
                                .build()))
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @SneakyThrows
    @Test
    void getUserByStudentCode_Should_ReturnOk() {
        var studentCode = "18120352";
        var request = get(buildUrl(String.format("/student-code?studentCode=%s", studentCode)))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(jwtUtils.buildAuthorizationHeader("admi"));
        var result = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        var response = objectMapper.readValue(result, new TypeReference<CommonResponse<UserSimpleResponse>>() {
        });
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EApiStatus.Success);
        var content = response.getContent();
        assertThat(content).isNotNull();
        assertThat(content.getStudentID()).isEqualTo(studentCode);
    }

    @SneakyThrows
    @Test
    void getUserByStudentCode_Should_ReturnNotFound() {
        var studentCode = "18120352a";
        var request = get(buildUrl(String.format("/student-code?studentCode=%s", studentCode)))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(jwtUtils.buildAuthorizationHeader("admi"));
        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

}