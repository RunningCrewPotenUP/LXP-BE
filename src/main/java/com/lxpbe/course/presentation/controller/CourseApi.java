package com.lxpbe.course.presentation.controller;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.course.presentation.docs.CourseApiResponseExamples;
import com.lxpbe.course.presentation.request.CreateCourseRequest;
import com.lxpbe.course.presentation.request.UpdateCourseRequest;
import com.lxpbe.course.presentation.response.CourseDetailResponse;
import com.lxpbe.course.presentation.response.CourseListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Course", description = "강좌 API")
public interface CourseApi {

    @Operation(summary = "강좌 검색")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.SEARCH_200)))
    })
    ResponseEntity<ApiResponse<Page<CourseListResponse>>> searchCourses(@ParameterObject Pageable pageable, String keyword);

    @Operation(summary = "강좌 생성")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.DETAIL_200))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "필수값 누락", value = CourseApiResponseExamples.CREATE_400),
                                    @ExampleObject(name = "강사 아님", value = CourseApiResponseExamples.INVALID_INSTRUCTOR_400)
                            }))
    })
    ResponseEntity<ApiResponse<CourseDetailResponse>> createCourse(Long instructorId, CreateCourseRequest request);

    @Operation(summary = "강좌 상세 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.DETAIL_200))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강좌 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.NOT_FOUND_404)))
    })
    ResponseEntity<ApiResponse<CourseDetailResponse>> getCourse(Long courseId);

    @Operation(summary = "강좌 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.DETAIL_200))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.UPDATE_DENIED_403))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강좌 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.NOT_FOUND_404)))
    })
    ResponseEntity<ApiResponse<CourseDetailResponse>> updateCourse(Long instructorId, Long courseId, UpdateCourseRequest request);

    @Operation(summary = "강좌 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.DELETE_DENIED_403))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강좌 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = CourseApiResponseExamples.NOT_FOUND_404)))
    })
    ResponseEntity<Void> deleteCourse(Long instructorId, Long courseId);
}
