package com.lxpbe.enrollment.application.facade.dto;

import java.util.Objects;

public record EnrollmentHistoryDto(
        Long enrollmentId,
        Long userId,
        Long courseId,
        String status
) {
    public EnrollmentHistoryDto {
        final String REQUIRED_FIELD_ERROR_MESSAGE = "EnrollmentHistoryDto 생성 실패 - 필수 필드 누락: ";
        Objects.requireNonNull(enrollmentId, REQUIRED_FIELD_ERROR_MESSAGE + "enrollmentId");
        Objects.requireNonNull(enrollmentId, REQUIRED_FIELD_ERROR_MESSAGE + "userId");
        Objects.requireNonNull(enrollmentId, REQUIRED_FIELD_ERROR_MESSAGE + "courseId");
        Objects.requireNonNull(enrollmentId, REQUIRED_FIELD_ERROR_MESSAGE + "status");
    }
}
