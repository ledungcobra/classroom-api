package com.ledungcobra.dto.course.removeMember;

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
public class RemoveMemberInCourseRequest implements Serializable {
    @NotNull
    private Integer courseId;
    private Integer userId;
    private String studentId;
    private String currentUser;
}
