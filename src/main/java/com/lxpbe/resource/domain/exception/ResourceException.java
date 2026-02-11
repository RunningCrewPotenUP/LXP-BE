package com.lxpbe.resource.domain.exception;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.exception.ErrorCode;

public class ResourceException extends DomainException {

    public ResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
