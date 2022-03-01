package com.ledungcobra.dto.course.index;

import com.ledungcobra.dto.course.getCourseById.CourseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseListWrapper implements Serializable {
    private List<CourseResponse> data;
    private boolean hasMore;
    private long total;
}
