package com.ledungcobra.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.common.BaseTest;
import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EStatus;
import com.ledungcobra.common.EResult;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.course.entity.Notification;
import com.ledungcobra.course.repository.NotificationRepository;
import com.ledungcobra.dto.notification.NotificationResponse;
import com.ledungcobra.dto.notification.NotificationsResponse;
import com.ledungcobra.user.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
class NotificationControllerTest extends BaseTest {


    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;

    private String testUser = "tanhank2k";

    @Test
    @SneakyThrows
    void getNotifications() {
        MvcResult result = mockMvc.perform(get(String.format("/notification?CurrentUser=%s&StartAt=%d&MaxResults=%d&SortColumn=-CreateOn", testUser, 0, 999))
                        .headers(jwtUtils.buildAuthorizationHeader(testUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var resString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(resString, new TypeReference<CommonResponse<NotificationsResponse>>() {
        });
        assertThat(res).isNotNull();
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getMessage()).isNotNull().isNotBlank();
        assertThat(res.getContent()).isNotNull();
        var content = res.getContent();
        assertThat(content.getTotal()).isEqualTo(11L);
        assertThat(content.getData()).isNotNull();
        var data = content.getData();
        assertThat(data.getAmountUnseen()).isEqualTo(5L);
        assertThat(data.getNotifications().size()).isEqualTo(11);
        for (NotificationResponse notification : data.getNotifications()) {
            assertThat(notification).isNotNull();
        }
    }

    @Test
    @SneakyThrows
    void putMarkNotificationSeen() {
        MvcResult result = mockMvc.perform(put("/notification/mark-seen/22").headers(jwtUtils.buildAuthorizationHeader(testUser)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var resString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(resString, new TypeReference<CommonResponse<String>>() {
        });
        assertThat(res).isNotNull();
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getMessage()).isNotBlank().isNotNull();

        var notification = notificationRepository.findById(22).orElse(null);
        assertThat(notification.getIsSeen()).isEqualTo((byte) 1);
    }

    @Test
    @SneakyThrows
    void putMarkNotificationsSeen() {
        MvcResult result = mockMvc.perform(put("/notification/mark-seen").headers(jwtUtils.buildAuthorizationHeader(testUser)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var resString = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        var res = objectMapper.readValue(resString, new TypeReference<CommonResponse<String>>() {
        });
        assertThat(res).isNotNull();
        assertThat(res.getStatus()).isEqualTo(EStatus.Success.getValue());
        assertThat(res.getResult()).isEqualTo(EResult.Successful.getValue());
        assertThat(res.getMessage()).isNotBlank().isNotNull();
        var notifications = notificationRepository.findAllByUserId(userService.findByUsername(testUser).getId());
        for (Notification not : notifications) {
            assertThat(not.getIsSeen()).isEqualTo((byte) 1);
        }
    }
}