package com.ledungcobra.dto.admin.login;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LoginArgs implements Serializable {
    private String username;
    private String password;
}
