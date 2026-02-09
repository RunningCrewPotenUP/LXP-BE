package com.lxpbe.course.controller;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.course.dto.CourseDetailResponse;
import com.lxpbe.course.dto.CourseListResponse;
import com.lxpbe.course.dto.CreateCourseRequest;
import com.lxpbe.course.dto.UpdateCourseRequest;
import com.lxpbe.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String dir,
            @RequestParam(required = false) String keyword
    ) {
        Sort.Direction direction = Sort.Direction.fromString(dir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<CourseListResponse> result = courseService.searchCourses(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(result, null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDetailResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long instructorId
    ) {
        CourseDetailResponse result = courseService.createCourse(request, instructorId);
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
