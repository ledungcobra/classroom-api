package com.ledungcobra.controller;

import com.ledungcobra.common.CommonResponse;
import com.ledungcobra.common.EApiStatus;
import com.ledungcobra.common.EResponseResult;
import com.ledungcobra.common.PageableBuilder;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.course.entity.Assignment;
import com.ledungcobra.course.entity.Course;
import com.ledungcobra.course.service.CourseService;
import com.ledungcobra.dto.course.index.CourseResponse;
import com.ledungcobra.dto.course.index.CourseWrapper;
import com.ledungcobra.dto.course.postCreateCourse.CreateCourseRequest;
import com.ledungcobra.dto.getAssignmentsOfCourse.AssignmentResponse;
import com.ledungcobra.dto.getAssignmentsOfCourse.AssignmentWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final JwtUtils jwtUtils;
    private final CourseService courseService;

    public CourseController(JwtUtils jwtUtils, CourseService courseService) {
        this.jwtUtils = jwtUtils;
        this.courseService = courseService;
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<?> getCourseById(@RequestParam(value = "Title", required = false) String title,
                                           @RequestParam(value = "StartAt", required = false) Integer startAt,
                                           @RequestParam(value = "MaxResults", required = false) Integer maxResults,
                                           @RequestParam(value = "SortColumn", required = false) String sortColumn,
                                           HttpServletRequest request) {

        var username = jwtUtils.getUserNameFromRequest(request);
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        title = title == null ? "%%" : "%" + title + "%";
        var index = (long) startAt + (long) maxResults;
        var count = courseService.countByOwner(username);
        var courses = courseService.getCoursesByTitleLike(title,
                PageableBuilder.getPageable(startAt, maxResults, sortColumn), username);
        return ResponseEntity.ok().body(CommonResponse.builder().content(CourseWrapper.builder()
                        .hasMore(index < count)
                        .data(courses.stream().map(CourseResponse::new).toList())
                        .total(count)
                        .build())
                .result(EResponseResult.Successful)
                .status(EApiStatus.Success)
                .message("Lấy dữ liệu khoá học thành công")
                .build()
        );
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<?> postCreateCourse(HttpServletRequest httpRequest, CreateCourseRequest request) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (currentUser == null) {
            return getNotFoundCurrentUser();
        }
        request.setCurrentUser(currentUser);
        var createdCourse = courseService.createCourse(request);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
                .content(new CourseResponse(createdCourse)).message("Tạo lớp học thành công")
                .build());
    }

    private ResponseEntity<?> getNotFoundCurrentUser() {
        return ResponseEntity.badRequest().body(CommonResponse.builder()
                .result(EResponseResult.Error)
                .status(EApiStatus.Error)
                .content("")
                .message("Không tìm thấy user").build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(HttpServletRequest httpRequest, @PathVariable("id") Integer id) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        if (currentUser == null) return getNotFoundCurrentUser();
        Course foundCourse = courseService.findCourseById(currentUser, id);
        if (foundCourse == null) return new ResponseEntity<>(CommonResponse.builder()
                .status(EApiStatus.Error)
                .result(EResponseResult.Error)
                .message("Class not found")
                .content("")
                .build(), HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
                .message("Get course success")
                .content(new CourseResponse(foundCourse))
                .build());
    }

    @GetMapping(value = "/{id}/assignments", produces = "application/json")
    public ResponseEntity<?> getAssignmentsOfCourse(HttpServletRequest httpRequest,
                                                    @PathVariable("id") Integer courseId,
                                                    @RequestParam(value = "Name", required = false) String name,
                                                    @RequestParam(value = "StartAt", required = false) Integer startAt,
                                                    @RequestParam(value = "MaxResults", required = false) Integer maxResults,
                                                    @RequestParam(value = "SortColumn", required = false) String sortColumn
    ) {
        var currentUser = jwtUtils.getUserNameFromRequest(httpRequest);
        name = name == null ? "%%" : "%" + name + "%";
        startAt = startAt == null ? 0 : startAt;
        maxResults = maxResults == null ? Integer.MAX_VALUE : maxResults;
        if (currentUser == null) return getNotFoundCurrentUser();
        List<Assignment> assignments = courseService
                .findAssignmentOfsCourseLikeName(currentUser, courseId, name, PageableBuilder.getPageable(startAt, maxResults, sortColumn));
        var total = courseService.findCourseById("tanhank2k", courseId).getAssignments().size();
        var index = startAt + maxResults;
        return ResponseEntity.ok(CommonResponse.builder()
                .message("Get assigment successfully")
                .status(EApiStatus.Success)
                .result(EResponseResult.Successful)
                .content(AssignmentWrapper.builder()
                        .total(total)
                        .hasMore(index < total)
                        .data(assignments.stream()
                                .map(AssignmentResponse::new)
                                .toList()
                        )
                        .build())
                .build());
    }


}
