package com.ledungcobra.dto.user.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import static com.ledungcobra.dto.ValidatorConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest implements Serializable {


    @NotNull(message = USER_NAME_NULL_MSG)
    @NotEmpty(message = USERNAME_EMPTY_MSG)
    private String username;

    @NotNull(message = PASSWORD_NULL_MSG)
    @NotEmpty(message = PASSWORD_EMPTY_MSG)
    private String password;

}
