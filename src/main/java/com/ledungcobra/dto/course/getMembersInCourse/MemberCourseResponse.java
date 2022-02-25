package com.ledungcobra.dto.course.getMembersInCourse;

import com.ledungcobra.dto.user.register.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCourseResponse implements Serializable {
    private Integer total;
    private String owner;
    private List<UserResponse> teachers;
    private List<UserResponse> students;
}
