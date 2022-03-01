package com.ledungcobra.configuration.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
public class WebSocketPrincipal implements Principal {
    private String name;
    private String username;

    public WebSocketPrincipal(String name) {
        this.name = name;
    }
}
