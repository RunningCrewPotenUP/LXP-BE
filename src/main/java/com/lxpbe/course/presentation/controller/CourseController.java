package com.lxpbe.course.presentation.controller;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.course.application.command.CourseCreateCommand;
import com.lxpbe.course.presentation.response.CourseDetailResponse;
import com.lxpbe.course.presentation.response.CourseListResponse;
import com.lxpbe.course.presentation.request.CreateCourseRequest;
import com.lxpbe.course.presentation.request.UpdateCourseRequest;
import com.lxpbe.course.application.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CourseListResponse>>> searchCourses(
            @PageableDefault(direction = Sort.Direction.DESC, sort = {"createdAt"}) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        Page<CourseListResponse> result = courseService.searchCourses(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(result, null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDetailResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long instructorId
    ) {
        CourseDetailResponse result = courseService.createCourse(CourseCreateCommand.of(instructorId, request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(result, null));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourse(
            @PathVariable Long courseId
    ) {
        CourseDetailResponse result = courseService.getCourse(courseId);
        return ResponseEntity.ok(new ApiResponse<>(result, null));
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> updateCourse(
            @PathVariable Long courseId,
            @RequestBody UpdateCourseRequest request
    ) {
        CourseDetailResponse result = courseService.updateCourse(courseId, request);
        return ResponseEntity.ok(new ApiResponse<>(result, null));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId
    ) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}
