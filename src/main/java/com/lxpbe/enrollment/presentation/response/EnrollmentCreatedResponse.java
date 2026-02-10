package com.lxpbe.enrollment.presentation.response;

import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

@Builder
public record EnrollmentCreatedResponse(
        long id,
        long courseId,
        EnrollmentStatus status,
        Instant enrolledAt
) {

    public EnrollmentCreatedResponse {
        Objects.requireNonNull(status, "EnrollmentCreatedResponse 생성 실패: status == null");
        Objects.requireNonNull(enrolledAt, "EnrollmentCreatedResponse 생성 실패: enrolledAt == null");
    }
}
