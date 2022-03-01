package com.ledungcobra.dto.user.register;

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
    private Integer role;
    private Integer gender;
    private String email;
    private String profileImageUrl;
    private String personalEmail;
    private String studentID;
    private String phoneNumber;
    private String personalPhoneNumber;
    private Integer userStatus;
    @EqualsAndHashCode.Exclude
    private String createOn;
    private String fullname;

    public UserResponse(User data) {
        id = data.getId();
        username = data.getUserName();
        profileImageUrl = data.getProfileImageUrl();
        email = data.getEmail();
        gender = data.getGender();
        studentID = data.getStudentID();
        role = data.getRole().getId();
        phoneNumber = data.getPhoneNumber();
        firstName = data.getFirstName();
        middleName = data.getMiddleName();
        lastName = data.getLastName();
        personalEmail = data.getPersonalEmail();
        personalPhoneNumber = data.getPersonalPhoneNumber();
        userStatus = data.getUserStatus();
        fullname = data.getNormalizedDisplayName();
        createOn = data.getCreateOn() != null ? data.getCreateOn().toString():"";
    }

    public UserResponse(Student student) {
        this.studentID = student.getStudentId();
        this.username = student.getFullName();
    }

}
