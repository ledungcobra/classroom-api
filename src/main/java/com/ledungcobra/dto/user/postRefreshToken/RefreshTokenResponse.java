package com.ledungcobra.dto.user.postRefreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class RefreshTokenResponse implements Serializable {
    private  String token;
    private String refreshToken;
}
