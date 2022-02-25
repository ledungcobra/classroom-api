package com.ledungcobra.dto.course.updateRoleMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleMemberInCourseRequest implements Serializable {
    private Integer courseId;
    private Integer userId;
    private Integer role;
    private String currentUser;
}
