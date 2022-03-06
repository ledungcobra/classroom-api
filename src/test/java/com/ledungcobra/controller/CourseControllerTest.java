package com.ledungcobra.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.*;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.websocket.WsNotificationController;
import com.ledungcobra.course.entity.Grade;
import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.course.entity.Student;
import com.ledungcobra.course.repository.AssignmentRepository;
import com.ledungcobra.course.repository.GradeRepository;
import com.ledungcobra.course.repository.NotificationRepository;
import com.ledungcobra.course.repository.StudentRepository;
import com.ledungcobra.dto.common.DataResponse;
import com.ledungcobra.dto.common.UpdateGradeSpecificRequest;
import com.ledungcobra.dto.course.getAllGrades.GradeResponseWrapper;
import com.ledungcobra.dto.course.getAllGradesV2.GradeOfAssignmentResponse;
import com.ledungcobra.dto.course.getAssignmentsOfCourse.AssignmentResponse;
import com.ledungcobra.dto.course.getAssignmentsOfCourse.AssignmentWrapper;
import com.ledungcobra.dto.course.getCourseById.CourseWrapperResponse;
import com.ledungcobra.dto.course.getGradeByStudent.SingleStudentGradeResponse;
import com.ledungcobra.dto.course.getMembersInCourse.MemberCourseResponse;
import com.ledungcobra.dto.course.index.CourseListWrapper;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.course.postSortAssignment.AssignmentSimple;
import com.ledungcobra.dto.course.postSortAssignment.SortAssignmentRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeNormalRequest;
import com.ledungcobra.dto.course.postUpdateAssignmentNormal.UpdateGradeSpecificRequestBase;
import com.ledungcobra.dto.course.removeMember.RemoveMemberInCourseRequest;
import com.ledungcobra.dto.course.updateAssigment.UpdateAssignmentsRequest;
import com.ledungcobra.dto.email.postSendMail.SendMailJoinToCourseRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
public class CourseControllerTest extends BaseTest {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;
    private String testUser = "tanhank2k";
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private WsNotificationController wsNotificationController;

    public static final UpdateAssignmentsRequest updateAssignmentsRequest_Success = UpdateAssignmentsRequest.builder()
            .name("name")
            .maxGrade(10)
            .description("description")
            .build();


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
        var response = objectMapper.readValue(responseString, new TypeReference<CommonResponse<CourseListWrapper>>() {
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
        var response = objectMapper.readValue(result, new TypeReference<CommonResponse<CourseWrapperResponse>>() {
        });
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(response.getResult()).isEqualTo(EResult.Successful.getValue());
        var course = response.getContent().getCourse();
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
        assertThat(response.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(response.getResult()).isEqualTo(EResult.Successful.getValue());
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

    @Test
    @SneakyThrows
    void updateAssignment() {
        var result = mockMvc.perform(put("/course/1/assignments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(jwtUtils.buildAuthorizationHeader(testUser))
                .content(objectMapper.writeValueAsString(updateAssignmentsRequest_Success))
        ).andDo(print()).andExpect(status().isOk()).andReturn();
        var response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<CommonResponse<AssignmentResponse>>() {
                });
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(response.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(response.getMessage()).isNotBlank().isNotNull();
        assertThat(response.getContent()).isNotNull();
        var content = response.getContent();
        assertThat(content.getCourseId()).isEqualTo(1);
        assertThat(content.getDescription()).isEqualTo(updateAssignmentsRequest_Success.getDescription());
        assertThat(content.getName()).isEqualTo(updateAssignmentsRequest_Success.getName());
        assertThat(content.getId()).isEqualTo(1);
        assertThat(content.getMaxGrade()).isEqualTo(updateAssignmentsRequest_Success.getMaxGrade());
    }


    @Test
    @SneakyThrows
    void postSortAssignment() {
        var firstAss = new AssignmentSimple(1, 3);
        var secondAss = new AssignmentSimple(2, 6);
        var result = mockMvc.perform(post("/course/1/assignments-sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(jwtUtils.buildAuthorizationHeader(testUser))
                        .content(objectMapper.writeValueAsString(SortAssignmentRequest.builder()
                                .currentUser("tanhank2k")
                                .assignmentSimples(List.of(firstAss, secondAss))
                                .build()))
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var retrievedFirstAss = assignmentRepository.findById(firstAss.getId()).get();
        var retrievedSecondAss = assignmentRepository.findById(secondAss.getId()).get();
        assertThat(retrievedSecondAss).isNotNull();
        assertThat(retrievedFirstAss).isNotNull();
        assertThat(retrievedFirstAss.getOrder()).isEqualTo(firstAss.getOrder());
        assertThat(retrievedSecondAss.getOrder()).isEqualTo(secondAss.getOrder());
    }

    @Test
    @SneakyThrows
    void getAllGrades() {
        var result = mockMvc
                .perform(get("/course/1/all-grades?currentUser=tanhank2k")
                        .headers(jwtUtils.buildAuthorizationHeader("tanhank2k")).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var commonResponse = objectMapper.readValue(responseString, new TypeReference<CommonResponse<GradeResponseWrapper>>() {
        });

        assertThat(commonResponse).isNotNull();
        assertThat(commonResponse.getMessage()).isNotNull().isNotBlank();
        assertThat(commonResponse.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(commonResponse.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(commonResponse.getContent()).isNotNull();
        var content = commonResponse.getContent();
        assertThat(content.getHeader()).isNotNull().hasSize(4);
        assertThat(content.getScores()).isNotNull().hasSize(5);
        assertThat(content.getTotal()).isEqualTo(5);
    }


    @Test
    @SneakyThrows
    void getGradeByStudent() {
        var result = mockMvc.perform(get("/course/1/all-grades/student")
                        .headers(jwtUtils.buildAuthorizationHeader("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print()).andReturn();
        var responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var response = objectMapper.readValue(responseString, new TypeReference<CommonResponse<SingleStudentGradeResponse>>() {
        });
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotNull();
        assertThat(response.getMessage()).isNotBlank().isNotNull();
        assertThat(response.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(response.getContent().getHeader()).isNotEmpty();
        assertThat(response.getContent().getScores()).isNotNull();
    }

    @Test
    @SneakyThrows
    void getAllGradesV2() {
        var result = mockMvc.perform(get("/course/1/assignments/1/all-grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(jwtUtils.buildAuthorizationHeader("tanhank2k"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var response = objectMapper.readValue(responseString, new TypeReference<CommonResponse<DataResponse<GradeOfAssignmentResponse>>>() {
        });
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(response.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(response.getMessage()).isNotNull().isNotBlank();
        assertThat(response.getContent()).isNotNull();
        var content = response.getContent();
        assertThat(content.getTotal()).isEqualTo(3);
        assertThat(content.getData().size()).isEqualTo(3);
    }

    @SneakyThrows
    @Test
    void postUpdateGradeFromFile() {
        var data = new Object[][]{
                new String[]{"StudentID", "Grade"},
                {"1", 10},
                {"2", 11},
                {"3", 20},
                {"4", 10},
                {"5", 7},
        };
        var courseId = 1;
        var assignmentId = 1;

        var book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet();

        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];
            XSSFRow rowExcel = sheet.createRow(i);
            rowExcel.createCell(0).setCellValue((String) row[0]);
            if (i == 0) {
                rowExcel.createCell(1)
                        .setCellValue(row[1].toString());
            } else {
                rowExcel.createCell(1).setCellValue(Float.parseFloat(row[1].toString()));
            }
        }
        var byteOutputStream = new ByteArrayOutputStream();
        book.write(byteOutputStream);
        MvcResult result = mockMvc.perform(multipart(String.format("/course/%d/assignments/%d/update-grade", courseId, assignmentId))
                .file("file", byteOutputStream.toByteArray())
                .headers(jwtUtils.buildAuthorizationHeader("tanhank2k"))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("CurrentUser", "tanhank2k")
        ).andExpect(status().isOk()).andDo(print()).andReturn();
        var responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(responseString, new TypeReference<CommonResponse<String>>() {
        });
        assertThat(res).isNotNull();
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());

        var next = true;
        for (Object[] row : data) {
            if (next) {
                next = false;
                continue;
            }
            Grade grade = gradeRepository.findGradeByStudentId(row[0].toString(), assignmentId, courseId);
            assertThat(grade.getGradeAssignment()).isEqualTo(Float.parseFloat(row[1].toString()));
        }
    }

    @SneakyThrows
    @Test
    void postUpdateStudentList() {
        var courseId = 1;
        var data = new Object[][]{
                {"StudentID", "FullName"},
                {"1", "Updated"},
                {"9999", "Hello world"}
        };
        var book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet();
        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];
            XSSFRow rowExcel = sheet.createRow(i);
            rowExcel.createCell(0).setCellValue(row[0].toString());
            rowExcel.createCell(1).setCellValue(row[1].toString());
        }
        var bos = new ByteArrayOutputStream();
        book.write(bos);
        var result = mockMvc.perform(multipart(String.format("/course/%d/update-student", courseId))
                        .file("file", bos.toByteArray())
                        .param("CurrentUser", "tanhank2k")
                        .headers(jwtUtils.buildAuthorizationHeader("tanhank2k"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var responseString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(responseString, new TypeReference<CommonResponse<String>>() {
        });
        assertThat(res).isNotNull();
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getMessage()).isNotBlank().isNotNull();
        for (int i = 1; i < data.length; i++) {
            Object[] row = data[i];
            Student student = studentRepository.findByStudentId(row[0].toString());
            assertThat(student.getFullName()).isEqualTo(row[1].toString());
        }
    }

    private HttpHeaders getAuthHeader() {
        return jwtUtils.buildAuthorizationHeader("tanhank2k");
    }


    @SneakyThrows
    @Test
    void postUpdateAssignmentNormal() {
        var countNotification = notificationRepository.count();
        doNothing().when(wsNotificationController).sendNotification(anyList());
        doNothing().when(wsNotificationController).sendNotification(any(Notification.class));

        var scores = List.of(
                UpdateGradeSpecificRequestBase.builder()
                        .grade(10)
                        .mssv("1")
                        .build(),
                UpdateGradeSpecificRequestBase.builder()
                        .grade(15)
                        .mssv("2")
                        .build()
        );
        var result = mockMvc.perform(post("/course/1/assignments/1/update-grade-normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateGradeNormalRequest.builder()
                                .isFinalized(true)
                                .currentUser("tanhank2k")
                                .scores(scores)
                                .build()))
                        .headers(getAuthHeader()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        var resString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(resString, new TypeReference<CommonResponse<Boolean>>() {
        });
        assertThat(res).isNotNull();
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getContent()).isTrue();
        assertThat(notificationRepository.count()).isEqualTo(countNotification + scores.size());
    }


    @Test
    @SneakyThrows
    void testPostUpdateGradeFinalized() {
        var notificationCount = notificationRepository.count();
        doNothing().when(wsNotificationController).sendNotification(anyList());
        doNothing().when(wsNotificationController).sendNotification(any(Notification.class));

        MvcResult result = mockMvc.perform(post("/course/1/assignments/1/update-grade-finalized")
                        .headers(getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateGradeSpecificRequest.builder()
                                .isFinalized(true)
                                .grade(10f)
                                .currentUser("tanhank2k")
                                .mssv("1")
                                .build()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var resString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(resString, new TypeReference<CommonResponse<Boolean>>() {
        });
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getContent()).isTrue();
        assertThat(notificationRepository.count()).isEqualTo(notificationCount + 1);
    }

    @Test
    @SneakyThrows
    void removeMember() {
        mockMvc.perform(
                        post("/course/remove-member")
                                .headers(jwtUtils.buildAuthorizationHeader(testUser))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(RemoveMemberInCourseRequest.builder()
                                        .currentUser(testUser)
                                        .courseId(1)
                                        .studentId("4")
                                        .build()))
                )
                .andExpect(status().isOk())
        ;
    }

    @Test
    @SneakyThrows
    void postSendMail() {
        MvcResult result = mockMvc.perform(post("/course/send-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SendMailJoinToCourseRequest.builder()
                                .classCode("1234567")
                                .mailPersonReceive("ledungcobra@gmail.com")
                                .role(ERole.Student.getValue())
                                .build()))
                        .headers(getAuthHeader()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        var resString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var response = objectMapper.readValue(resString, new TypeReference<CommonResponse<String>>() {
        });
        assertThat(response.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(response.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(response.getMessage()).isNotNull().isNotBlank();
    }

    @Test
    @SneakyThrows
    void getMembersInCourse() {
        MvcResult result = mockMvc.perform(get("/course/1/everyone").headers(getAuthHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var res = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<CommonResponse<MemberCourseResponse>>() {
        });

        assertThat(res).isNotNull();
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getMessage()).isNotNull().isNotBlank();
        assertThat(res.getContent()).isNotNull();
        var content = res.getContent();
        assertThat(content.getTotal()).isEqualTo(6);
        assertThat(content.getOwner()).isEqualTo("tanhank2k");
        assertThat(content.getTeachers()).hasSize(1);
        assertThat(content.getStudents()).hasSize(5);
    }
}