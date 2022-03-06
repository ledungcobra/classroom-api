package com.ledungcobra.dto.gradereview.postCreateGradeReview;

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
public class CreateGradeReviewRequest implements Serializable {

    @NotNull
    private Integer courseId;

    @NotNull
    private Integer gradeId;

    @NotNull
    private Float gradeExpect;

    @NotNull
    @NotBlank
    private String reason;

    private String currentUser;

}
