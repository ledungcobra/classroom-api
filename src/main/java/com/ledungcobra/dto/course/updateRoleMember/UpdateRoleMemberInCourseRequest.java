package com.ledungcobra.dto.course.updateRoleMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleMemberInCourseRequest implements Serializable {
    @NotNull
    private Integer courseId;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer role;
    private String currentUser;
}
