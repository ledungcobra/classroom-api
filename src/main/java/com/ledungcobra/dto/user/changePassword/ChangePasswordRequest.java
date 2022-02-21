package com.ledungcobra.dto.user.changePassword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest implements Serializable {
    private String currentPassword;
    private String newPassword;
    @Deprecated
    private String currentUser;
}
