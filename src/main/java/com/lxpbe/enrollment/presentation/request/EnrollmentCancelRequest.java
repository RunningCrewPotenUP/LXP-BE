package com.lxpbe.enrollment.presentation.request;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record EnrollmentCancelRequest(
        @NotNull(message = "enrollmentId 는 필수입니다.")
        @Positive(message = "enrollmentId 는 음수일 수 없습니다.")
        @Schema(example = "1")
        Long enrollmentId,

        @NotNull(message = "취소 사유 유형은 필수입니다.")
        @Schema(example = "PURCHASE_MISTAKE")
        CancelReasonType reasonType,

        @Schema(example = "착오로 인한 취소입니다.")
        String reason
) {
}
