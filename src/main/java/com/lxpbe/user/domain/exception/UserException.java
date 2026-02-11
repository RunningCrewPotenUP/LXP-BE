package com.lxpbe.user.domain.exception;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.exception.ErrorCode;

public class UserException extends DomainException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
