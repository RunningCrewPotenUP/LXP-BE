package com.lxpbe.enrollment.domain.event;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record EnrollmentCancelled(
        Long enrollmentId,
        Long userId,
        Long courseId,
        Instant cancelledAt,
        CancelType cancelType,
        CancelReasonType cancelReasonType,
        String cancelReasonComment
) {
}
