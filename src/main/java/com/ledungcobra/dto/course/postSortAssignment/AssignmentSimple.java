package com.ledungcobra.dto.course.postSortAssignment;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentSimple implements Serializable {
    private Integer id;
    private Integer order;
}
