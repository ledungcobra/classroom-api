package com.ledungcobra.dto.gradereview.GradeReviewResponse;

import com.ledungcobra.course.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse implements Serializable {
    private Integer id;
    private String studentID;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBird;
    private Integer userId;
    private String phone;

    public StudentResponse(Student student) {
        id = student.getId();
        studentID = student.getStudentId();
        fullName = student.getFullName();
        firstName = student.getFirstName();
        middleName = student.getMiddleName();
        lastName = student.getLastName();
        dateOfBird = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "";
        phone = student.getPhone();
        userId = 0;
    }
}
