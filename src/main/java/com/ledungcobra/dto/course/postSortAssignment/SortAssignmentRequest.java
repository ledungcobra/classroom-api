package com.ledungcobra.dto.course.postSortAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortAssignmentRequest implements Serializable {
    private List<AssignmentSimple> assignmentSimples;
    private String currentUser;
}
