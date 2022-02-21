package com.ledungcobra.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.BaseTest;
import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EApiStatus;
import com.ledungcobra.common.EResponseResult;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.dto.course.index.CourseResponse;
import com.ledungcobra.dto.course.index.CourseWrapper;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.getAssignmentsOfCourse.AssignmentResponse;
import com.ledungcobra.dto.getAssignmentsOfCourse.AssignmentWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
class CourseControllerTest extends BaseTest {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;
    private String testUser = "tanhank2k";

    @SneakyThrows
    @Test
    void createCourse_Should_Success() {
        mockMvc.perform(post("/course").contentType(MediaType.APPLICATION_JSON)
                        .headers(jwtUtils.buildAuthorizationHeader(testUser))
                        .content(objectMapper.writeValueAsString(CreateCourseRequest.builder().build())))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

    }

    @Test
    @SneakyThrows
    void getCourse_Should_Success() {

        var result = mockMvc.perform(get("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(jwtUtils.buildAuthorizationHeader(testUser)));
        var responseString = result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        var response = objectMapper.readValue(responseString, new TypeReference<CommonResponse<CourseWrapper>>() {
        });
        var courseWapper = response.getContent();
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isNotNull().isNotBlank();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getResult()).isEqualTo(1);
        assertThat(courseWapper).isNotNull();
        assertThat(courseWapper.getTotal()).isEqualTo(7);
        assertThat(courseWapper.isHasMore()).isFalse();
        assertThat(courseWapper.getData()).isNotNull().hasSize(7);
    }

    @Test
    @SneakyThrows
    void getCourseById() {
        var result = mockMvc.perform(get("/course/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(jwtUtils.buildAuthorizationHeader("tanhank2k"))
        ).andDo(print()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        var response = objectMapper.readValue(result, new TypeReference<CommonResponse<CourseResponse>>() {
        });
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EApiStatus.Success.getValue());
        assertThat(response.getResult()).isEqualTo(EResponseResult.Successful.getValue());
        var course = response.getContent();
        assertThat(course).isNotNull();
        assertThat(course.getId()).isEqualTo(1);
        assertThat(course.getClassCode()).isNotNull().isNotBlank();
        assertThat(course.getDescription()).isNotNull().isNotBlank();
        assertThat(course.getTitle()).isNotNull().isNotBlank();
    }

    @Test
    @SneakyThrows
    void getAssignmentsOfCourse() {
        var result = mockMvc.perform(get("/course/1/assignments")
                        .headers(jwtUtils.buildAuthorizationHeader(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<CommonResponse<AssignmentWrapper>>() {
        });
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EApiStatus.Success.getValue());
        assertThat(response.getResult()).isEqualTo(EResponseResult.Successful.getValue());
        assertThat(response.getMessage()).isNotBlank().isNotNull();
        assertThat(response.getContent()).isNotNull();
        var assignments = response.getContent();
        assertThat(assignments.isHasMore()).isFalse();
        assertThat(assignments.getTotal()).isEqualTo(4);
        assertThat(assignments.getData().size()).isEqualTo(4);
        for (AssignmentResponse assignment : assignments.getData()) {
            assertThat(assignment).isNotNull();
        }
    }
}