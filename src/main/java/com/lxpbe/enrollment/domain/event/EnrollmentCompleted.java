package com.lxpbe.enrollment.domain.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record EnrollmentCompleted(
        Long enrollmentId,
        Long userId,
        Long courseId,
        Instant completedAt
) {
}
