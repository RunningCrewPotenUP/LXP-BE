package com.lxpbe.enrollment.application.policy;

import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import org.springframework.stereotype.Component;

@Component
public class DefaultEnrollmentCancelPolicy implements EnrollmentCancelPolicy {

    @Override
    public void cancel(
            Enrollment target,
            CancelType cancelType,
            CancelReasonType cancelReasonType,
            String cancelReasonComment
    ) {
        target.cancel(cancelType, cancelReasonType, cancelReasonComment);
    }
}
