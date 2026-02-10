package com.lxpbe.enrollment.domain.model.enums;

import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;

public enum EnrollmentStatus {

    ENROLLED,       // 수강 등록 되었으나, 수강 시작 전
    IN_PROGRESS,    // 수강 중
    COMPLETED,      // 수료
    CANCELLED       // 수강 취소
    ;

    public EnrollmentStatus toInProgress() {
        if (this.equals(EnrollmentStatus.ENROLLED)) {
            return EnrollmentStatus.IN_PROGRESS;
        }

        if (this.equals(EnrollmentStatus.IN_PROGRESS)) {
            throw new EnrollmentException(EnrollmentErrorCode.ALREADY_IN_PROGRESS);
        }

        throw new EnrollmentException(EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_IN_PROGRESS);
    }

    public EnrollmentStatus toCancelled() {
        if (this.equals(EnrollmentStatus.ENROLLED)
                || this.equals(EnrollmentStatus.IN_PROGRESS)) {
            return EnrollmentStatus.CANCELLED;
        }

        if (this.equals(EnrollmentStatus.CANCELLED)) {
            throw new EnrollmentException(EnrollmentErrorCode.ALREADY_CANCELLED);
        }

        throw new EnrollmentException(EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_CANCELLED);
    }

    public EnrollmentStatus toCompleted() {
        if (this.equals(EnrollmentStatus.IN_PROGRESS)) {
            return EnrollmentStatus.COMPLETED;
        }

        if (this.equals(EnrollmentStatus.COMPLETED)) {
            throw new EnrollmentException(EnrollmentErrorCode.ALREADY_COMPLETED);
        }

        throw new EnrollmentException(EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_COMPLETED);
    }
}
