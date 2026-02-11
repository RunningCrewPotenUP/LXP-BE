package com.lxpbe.enrollment.presentation.response;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record EnrollmentCancelledResponse(
        Long id,
        Long courseId,
        EnrollmentStatus status,
        Instant enrolledAt,
        Instant learningStartedAt,
        Instant cancelledAt,
        CancelType cancelType,
        CancelReasonType reasonType,
        String reason
) {
}
