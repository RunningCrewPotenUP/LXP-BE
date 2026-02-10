package com.lxpbe.enrollment.application.policy;

import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;

public interface EnrollmentCancelPolicy {

    void cancel(
            Enrollment target,
            CancelType cancelType,
            CancelReasonType cancelReasonType,
            String cancelReasonComment
    );
}
