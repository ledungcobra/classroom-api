package com.ledungcobra.dto.course.getCourseById;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseWrapperResponse implements Serializable {
    private CourseResponse course;
    private Integer role;
}