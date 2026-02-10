package com.lxpbe.enrollment.domain.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record EnrollmentCreated(
        Long enrollmentId,
        Long userId,
        Long courseId,
        Instant createdAt
) {
}
