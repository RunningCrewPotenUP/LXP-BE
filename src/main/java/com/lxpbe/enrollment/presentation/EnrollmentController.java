package com.lxpbe.enrollment.presentation;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.common.security.LoginUser;
import com.lxpbe.enrollment.application.EnrollmentCommandService;
import com.lxpbe.enrollment.application.EnrollmentQueryService;
import com.lxpbe.enrollment.application.command.EnrollmentCancelCommand;
import com.lxpbe.enrollment.application.result.EnrollmentDetails;
import com.lxpbe.enrollment.application.result.EnrollmentSummary;
import com.lxpbe.enrollment.presentation.request.EnrollmentCancelRequest;
import com.lxpbe.enrollment.application.result.EnrollmentCancelledResult;
import com.lxpbe.enrollment.application.result.EnrollmentCreatedResult;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController implements EnrollmentApi {

    private final EnrollmentCommandService enrollmentCommandService;
    private final EnrollmentQueryService enrollmentQueryService;

    public EnrollmentController(EnrollmentCommandService enrollmentCommandService, EnrollmentQueryService enrollmentQueryService) {
        this.enrollmentCommandService = enrollmentCommandService;
        this.enrollmentQueryService = enrollmentQueryService;
    }

    // ----- 수강 등록

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentCreatedResult>> enroll(
            @LoginUser
            Long userId,

            @RequestParam
            @NotNull(message = "courseId 는 필수입니다.")
            @Positive(message = "courseId 는 음수일 수 없습니다.")
            Long courseId
    ) {
        EnrollmentCreatedResult response = enrollmentCommandService.enroll(userId, courseId);
        return ResponseEntity
                .created(URI.create("/enrollments/" + response.id()))
                .body(ApiResponse.success(response));
    }

    // ----- 수강 취소

    @PatchMapping
    public ResponseEntity<ApiResponse<EnrollmentCancelledResult>> cancelByUser(
            @LoginUser
            Long userId,
            @RequestBody
            EnrollmentCancelRequest request
    ) {
        EnrollmentCancelledResult response
                = enrollmentCommandService.cancelByUser(EnrollmentCancelCommand.of(userId, request));
        return ResponseEntity
                .ok(ApiResponse.success(response));
    }

    // ----- 나의 수강 조회

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentSummary>>> getMyEnrollments(@LoginUser Long userId) {

        List<EnrollmentSummary> response = enrollmentQueryService.myEnrollments(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<ApiResponse<EnrollmentDetails>> getMyEnrollmentDetails(
            @LoginUser
            Long userId,
            @PathVariable
            Long enrollmentId
    ) {
        EnrollmentDetails response = enrollmentQueryService.queryDetails(userId, enrollmentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
