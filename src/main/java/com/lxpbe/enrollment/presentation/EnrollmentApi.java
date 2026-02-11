package com.lxpbe.enrollment.presentation;

import com.lxpbe.enrollment.application.result.EnrollmentCancelledResult;
import com.lxpbe.enrollment.application.result.EnrollmentCreatedResult;
import com.lxpbe.enrollment.application.result.EnrollmentDetails;
import com.lxpbe.enrollment.application.result.EnrollmentSummary;
import com.lxpbe.enrollment.presentation.docs.EnrollmentApiResponseExamples;
import com.lxpbe.enrollment.presentation.request.EnrollmentCancelRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Enrollment", description = "수강 등록 API")
public interface EnrollmentApi {

    @Operation(summary = "수강 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "수강 등록 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.ENROLL_201))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.ENROLL_400))),
            @ApiResponse(responseCode = "409", description = "중복 수강 등록",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.ENROLL_409)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<EnrollmentCreatedResult>> enroll(Long userId, Long courseId);

    @Operation(summary = "수강 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수강 취소 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.CANCEL_200))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.CANCEL_400))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.CANCEL_FORBIDDEN_403))),
            @ApiResponse(responseCode = "404", description = "수강 등록 정보 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.CANCEL_404)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<EnrollmentCancelledResult>> cancelByUser(Long userId, EnrollmentCancelRequest request);

    @Operation(summary = "내 수강 목록 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수강 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.QUERY_SUMMARIES_200)
                    )
            )
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<List<EnrollmentSummary>>> getMyEnrollments(Long userId);

    @Operation(summary = "내 수강 상세 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수강 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EnrollmentApiResponseExamples.QUERY_DETAILS_200)
                    )
            )
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<EnrollmentDetails>> getMyEnrollmentDetails(Long userId, Long enrollmentId);
}
