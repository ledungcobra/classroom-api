package com.ledungcobra.configuration.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Log4j2
public abstract class BaseWebSocketController {

    public static final String ADD_COMMENT = "ADD_COMMENT";
    public static final String UPDATE_COMMENT = "UPDATE_COMMENT";
    public static final String DELETE_COMMENT = "DELETE_COMMENT";
    public static final String APPROVAL = "APPROVAL";
    protected final SimpMessagingTemplate template;
    protected final ObjectMapper objectMapper;
    public static final String CHANNEL = "channel";

    protected final Map<String, WebSocketPrincipal> connections = new ConcurrentHashMap<>();
    protected final Map<Integer, String> userIDToUserNameMap = new ConcurrentHashMap<>();
    protected static final String MESSAGES = "/Messages";
    protected static final String NOTIFICATIONS = "/Notifications";

    public BaseWebSocketController(SimpMessagingTemplate template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    public abstract void onMessage(@Payload String message, Principal principal,
                                   @Headers Map<String, Object> header
    );

    @SneakyThrows
    public void onMessageMustCall(String message, Principal principal,
                                  Map<String, Object> header) {
        log.info("Recveive {}", message);
        var data = objectMapper.readValue(message, MessageBody.class);
        switch (data.getChannel()) {
            case "JOIN" -> {
                synchronized (connections) {
                    var username = data.getData().get("username").toString();
                    connections.computeIfAbsent(username, s -> (WebSocketPrincipal) principal);
                    var clientId = Integer.parseInt(data.getData().get("sender").toString());
                    userIDToUserNameMap.put(clientId, username);
                    sendMessageToClient(clientId, objectMapper.writeValueAsString(MessageBody.builder()
                            .channel("SUCCESS")
                            .build()));
                }
                break;
            }
            case "DISCONNECT" -> connections.remove(data.getData().get("username").toString());
            case "CHAT" -> {
                synchronized (connections) {
                    var receiver = data.getData().get("receiver").toString();
                    if (connections.containsKey(receiver)) {
                        sendMessageToClient(receiver, message);
                    }
                }
            }
            default -> {
                log.info("Not map");
            }
        }
    }

    protected void sendMessageToClient(String receiver, String message) {

        try {
            if (!connections.containsKey(receiver)) {
                return;
            }
            template.convertAndSendToUser(connections.get(receiver).getName(), getChannel(), message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessageToClient(int clientId, String message) {
        sendMessageToClient(this.userIDToUserNameMap.get(clientId), message);
    }


    @MessageExceptionHandler
    protected void exceptionHandler(Exception exception) {
        exception.printStackTrace();
    }

    protected abstract String getChannel();
}

