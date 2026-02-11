package com.lxpbe.auth.domain.exception;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.exception.ErrorCode;

public class AuthException extends DomainException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
