package com.lxpbe.enrollment.presentation.response;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

@Builder
public record EnrollmentCancelledResponse(
        Long id,
        Long courseId,
        EnrollmentStatus status,
        Instant enrolledAt,
        Instant learningStartedAt,  // optional
        Instant cancelledAt,
        CancelType cancelType,
        CancelReasonType reasonType,
        String reason
) {
    public EnrollmentCancelledResponse {
        final String REQUIRED_FIELD_ERROR_MESSAGE_PREFIX = "수강 취소 성공 응답 시 필수 필드 누락: ";
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "id");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "courseId");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "status");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "enrolledAt");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "cancelledAt");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "cancelType");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "reasonType");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "reason");
    }
}
