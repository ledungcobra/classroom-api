package com.ledungcobra.dto.getAssignmentsOfCourse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentWrapper implements Serializable {
    private List<AssignmentResponse> data;
    private boolean hasMore;
    private long total;
}