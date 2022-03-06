package com.ledungcobra.dto.user.changePassword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest implements Serializable {

    @NotNull
    @NotBlank
    private String currentPassword;

    @NotNull
    @NotBlank
    private String newPassword;

    @Deprecated
    private String currentUser;
}
