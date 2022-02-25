package com.ledungcobra.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGradeSpecificArgs {
    private Integer assignmentId;
    private Integer courseId;
    private Boolean isFinalized;
    private String mssv;
    private String currentUser;
    private Float gradeAssignment;
}
