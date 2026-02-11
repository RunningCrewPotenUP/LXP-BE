package com.lxpbe.course.domain.exception;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.exception.ErrorCode;

public class CourseException extends DomainException {

    public CourseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
