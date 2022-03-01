package com.ledungcobra.configuration.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledungcobra.course.entity.Notification;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class WsNotificationController extends BaseWebSocketController {

    public static final String NOTIFICATION = "NOTIFICATION";

    public WsNotificationController(SimpMessagingTemplate template, ObjectMapper objectMapper) {
        super(template, objectMapper);
    }

    @MessageMapping(NOTIFICATIONS)
    @Override
    public void onMessage(@Payload String message, Principal principal, @Headers Map<String, Object> header) {
        onMessageMustCall(message, principal, header);
    }

    @SneakyThrows
    public <T extends Map<String, Object>> void sendNotification(List<Notification> notifications) {
        for (Notification notification : notifications) {
            sendMessageToClient(notification.getUserId(), objectMapper.writeValueAsString(MessageBody.builder()
                    .channel(NOTIFICATION)
                    .data(MessageBody.fromObject(notification))
                    .build()));
        }
    }

    @SneakyThrows
    public <T extends Map<String, Object>> void sendNotification(Notification notification) {
        sendMessageToClient(notification.getUserId(), objectMapper.writeValueAsString(MessageBody.builder()
                .channel(NOTIFICATION)
                .data(MessageBody.fromObject(notification))
                .build()));
    }

}
