package com.ledungcobra.dto.course.postSortAssignment;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentSimple implements Serializable {
    @NotNull
    private Integer id;
    @NotNull
    private Integer order;
}
