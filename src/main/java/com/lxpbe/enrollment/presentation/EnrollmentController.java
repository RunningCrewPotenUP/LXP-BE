package com.lxpbe.enrollment.presentation;

import com.lxpbe.enrollment.application.EnrollmentCommandService;
import com.lxpbe.enrollment.application.EnrollmentQueryService;
import com.lxpbe.enrollment.application.command.EnrollmentCancelCommand;
import com.lxpbe.enrollment.presentation.request.EnrollmentCancelRequest;
import com.lxpbe.enrollment.presentation.response.EnrollmentCancelledResponse;
import com.lxpbe.enrollment.presentation.response.EnrollmentCreatedResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentCommandService enrollmentCommandService;
    private final EnrollmentQueryService enrollmentQueryService;

    public EnrollmentController(EnrollmentCommandService enrollmentCommandService, EnrollmentQueryService enrollmentQueryService) {
        this.enrollmentCommandService = enrollmentCommandService;
        this.enrollmentQueryService = enrollmentQueryService;
    }

    // ----- 수강 등록

    @PostMapping
    public ResponseEntity<EnrollmentCreatedResponse> enroll(
            @LoginUser
            Long userId,

            @RequestParam
            @NotNull(message = "courseId 는 필수입니다.")
            @Positive(message = "courseId 는 음수일 수 없습니다.")
            Long courseId
    ) {
        EnrollmentCreatedResponse response = enrollmentCommandService.enroll(userId, courseId);
        return ResponseEntity
                .created(URI.create("/enrollments/" + response.id()))
                .body(response);
    }

    // ----- 수강 취소

    @PatchMapping
    public ResponseEntity<EnrollmentCancelledResponse> cancelByUser(
            @LoginUser
            Long userId,
            @RequestBody
            EnrollmentCancelRequest request
    ) {
        EnrollmentCancelledResponse response
                = enrollmentCommandService.cancelByUser(EnrollmentCancelCommand.of(userId, request));
        return ResponseEntity
                .ok()
                .body(response);
    }
}

/**
 * Stub
 * - 지윤님 pr 머지되면 대체할 것
 */
@interface LoginUser {
}
