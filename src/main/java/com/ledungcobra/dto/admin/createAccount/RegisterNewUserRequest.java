package com.ledungcobra.dto.admin.createAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterNewUserRequest implements Serializable {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String studentID;
    private String normalizedDisplayName;
    private String personalEmail;
    private boolean personalEmailConfirmed;
}
