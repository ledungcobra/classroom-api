package com.ledungcobra.dto.admin.registerAdmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterNewUserRequest implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String personalEmail;
    private String personalPhoneNumber;
    private String studentID;
    private String username;
    private String password;
}
