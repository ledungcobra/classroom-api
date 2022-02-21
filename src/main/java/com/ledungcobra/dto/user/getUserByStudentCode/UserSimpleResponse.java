package com.ledungcobra.dto.user.getUserByStudentCode;

import com.ledungcobra.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSimpleResponse implements Serializable {

    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String profileImageUrl;
    private String studentID;

    public UserSimpleResponse(User user) {
        username = user.getUserName();
        firstName = user.getFirstName();
        middleName = user.getMiddleName();
        lastName = user.getLastName();
        profileImageUrl = user.getProfileImageUrl();
        studentID = user.getStudentID();
    }
}
