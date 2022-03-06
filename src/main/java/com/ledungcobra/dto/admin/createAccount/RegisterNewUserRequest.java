package com.ledungcobra.dto.admin.createAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static com.ledungcobra.dto.ValidatorConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterNewUserRequest implements Serializable {


    @NotNull
    @NotBlank(message = USERNAME_CANNOT_EMPTY)
    private String username;

    @NotNull
    @Email(message = EMAIL_VALIDATION_MESSAGE)
    private String email;

    @Length(min = 8)
    @Pattern(regexp = PASSWORD_PATTERN,message = PASSWORD_VALIDATION_MESSAGE)
    private String password;

    @Pattern(regexp = PHONE_NUMBER_PATTERN,message = PHONE_NUMBER_VALIDATION_MESSAGE)
    private String phoneNumber;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String middleName;

    @NotNull
    @NotBlank
    private String lastName;

}
