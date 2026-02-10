package com.lxpbe.enrollment.application.command;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.presentation.request.EnrollmentCancelRequest;
import lombok.Builder;

@Builder
public record EnrollmentCancelCommand(
        Long userId,
        Long enrollmentId,
        CancelReasonType reasonType,
        String reason
) {
    public static EnrollmentCancelCommand of(Long userId, EnrollmentCancelRequest request) {
        return EnrollmentCancelCommand.builder()
                .enrollmentId(request.enrollmentId())
                .reasonType(request.reasonType())
                .reason(request.reason())
                .build();
    }
}
