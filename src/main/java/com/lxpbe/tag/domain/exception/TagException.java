package com.lxpbe.tag.domain.exception;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.exception.ErrorCode;

public class TagException extends DomainException {

    public TagException(ErrorCode errorCode) {
        super(errorCode);
    }
}
