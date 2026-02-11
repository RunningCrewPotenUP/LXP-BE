package com.lxpbe.enrollment.presentation.request;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record EnrollmentCancelRequest(
        @NotNull(message = "enrollmentId 는 필수입니다.")
        @Positive(message = "enrollmentId 는 음수일 수 없습니다.")
        Long enrollmentId,

        @NotNull(message = "취소 사유 유형은 필수입니다.")
        CancelReasonType reasonType,

        String reason
) {
}
