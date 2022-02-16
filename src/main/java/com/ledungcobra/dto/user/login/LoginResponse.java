package com.ledungcobra.dto.user.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String fullName;
    private String email;
    private String token;
    private Integer id;
    private String refreshToken;
}
