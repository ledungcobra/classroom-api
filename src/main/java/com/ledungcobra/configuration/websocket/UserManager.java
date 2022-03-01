package com.ledungcobra.configuration.websocket;

import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserManager {
    private Set<Principal> users;

    public UserManager() {
        users = new HashSet<>();
    }

    public void addUser(Principal user) {
        users.add(user);
    }

    public void remove(Principal user) {
        users.remove(user);
    }

}
