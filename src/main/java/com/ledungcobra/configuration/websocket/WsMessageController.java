package com.ledungcobra.configuration.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class WsMessageController extends BaseWebSocketController{

    public WsMessageController(SimpMessagingTemplate template, ObjectMapper objectMapper) {
        super(template, objectMapper);
    }

    @MessageMapping(MESSAGES)
    @Override
    public void onMessage(@Payload String message, Principal principal,
                          @Headers Map<String, Object> header) {
        onMessageMustCall(message,principal,header);
    }

    @SneakyThrows
    public   void addComment(int id, MessageBody response) {
        response.setChannel(ADD_COMMENT);
        sendMessageToClient(id, objectMapper.writeValueAsString(response));
    }

    @SneakyThrows
    public void updateComment(int id, MessageBody response) {
        response.setChannel(UPDATE_COMMENT);
        sendMessageToClient(id, objectMapper.writeValueAsString(response));
    }

    @SneakyThrows
    public  void deleteComment(int id, MessageBody response) {
        response.setChannel(DELETE_COMMENT);
        sendMessageToClient(id, objectMapper.writeValueAsString(response));
    }

    @SneakyThrows
    public void sendApproval(int id, MessageBody response) {
        response.setChannel(APPROVAL);
        sendMessageToClient(id, objectMapper.writeValueAsString(response));
    }

    @Override
    protected String getChannel() {
        return MESSAGES;
    }
}
