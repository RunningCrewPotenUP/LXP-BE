package com.lxpbe.enrollment.domain.exception;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.exception.ErrorCode;

public class EnrollmentException extends DomainException {

    public EnrollmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
