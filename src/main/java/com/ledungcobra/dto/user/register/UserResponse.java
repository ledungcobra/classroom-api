package com.ledungcobra.dto.user.register;

import com.ledungcobra.common.EGender;
import com.ledungcobra.common.ERoleAccount;
import com.ledungcobra.common.EUserStatus;
import com.ledungcobra.course.entity.Student;
import com.ledungcobra.user.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserResponse implements Serializable {
    private int id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    @EqualsAndHashCode.Exclude
    private ERoleAccount role;
    private EGender gender;
    private String email;
    private String profileImageUrl;
    private String personalEmail;
    private String studentID;
    private String phoneNumber;
    private String personalPhoneNumber;
    private EUserStatus userStatus;
    @EqualsAndHashCode.Exclude
    private String createOn;
    private String fullname;

    public UserResponse(User data) {
        id = data.getId();
        username = data.getUserName();
        profileImageUrl = data.getProfileImageUrl();
        email = data.getEmail();
        gender = EGender.from(data.getGender());
        studentID = data.getStudentID();
        role = ERoleAccount.from(data.getRole() != null ? data.getRole().getId() : 0);
        phoneNumber = data.getPhoneNumber();
        firstName = data.getFirstName();
        middleName = data.getMiddleName();
        lastName = data.getLastName();
        personalEmail = data.getPersonalEmail();
        personalPhoneNumber = data.getPersonalPhoneNumber();
        userStatus = EUserStatus.from(data.getUserStatus());
        createOn = data.getCreateOn().toString();
        fullname = data.getNormalizedDisplayName();
    }

    public UserResponse(Student student) {
        this.studentID = student.getStudentId();
        this.username = student.getFullName();
    }

}
