package com.ledungcobra.dto.course.addStudentIntoCourseByLink;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMemberIntoCourseByLinkRequest implements Serializable {
    private String token;
    private Integer role;
    private String currentUser;
    private String invitee;
}
