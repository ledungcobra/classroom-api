package com.ledungcobra.dto.course.postSortAssignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortAssignmentRequest implements Serializable {
    @Size(min = 2,max = 2)
    private List<AssignmentSimple> assignmentSimples;
    private String currentUser;
}
