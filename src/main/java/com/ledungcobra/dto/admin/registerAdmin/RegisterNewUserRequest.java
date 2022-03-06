package com.ledungcobra.dto.admin.registerAdmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static com.ledungcobra.dto.ValidatorConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterNewUserRequest implements Serializable {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String middleName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Email(message = EMAIL_VALIDATION_MESSAGE)
    private String email;

    @Pattern(regexp = PHONE_NUMBER_PATTERN,message = PHONE_NUMBER_VALIDATION_MESSAGE)
    private String phoneNumber;

    @NotNull
    @NotBlank
    private String username;

    @Pattern(regexp = PASSWORD_PATTERN,message = PASSWORD_VALIDATION_MESSAGE)
    private String password;
}
