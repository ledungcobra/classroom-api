package com.ledungcobra.dto.course.addStudentIntoCourseByLink;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddMemberIntoCourseByLinkRequest implements Serializable {

    @NotNull
    @NotBlank
    private String token;

    @NotNull
    private Integer role;

    private String currentUser;

    private String invitee;

}
