package com.lxpbe.enrollment.application.result;

import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

@Builder
public record EnrollmentCreatedResult(
        Long id,
        Long courseId,
        EnrollmentStatus status,
        Instant enrolledAt
) {

    public EnrollmentCreatedResult {
        final String REQUIRED_FIELD_ERROR_MESSAGE_PREFIX = "수강 생성 성공 응답 시 필수 필드 누락: ";
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "id");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "courseId");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "status");
        Objects.requireNonNull(enrolledAt, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "enrolledAt");
    }
}
