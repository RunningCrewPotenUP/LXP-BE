package com.lxpbe.common.exception;

public class DomainException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, String additionalInfo) {
        super(errorCode.message() + ", " + additionalInfo);
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, String additionalInfo, Throwable cause) {
        super(errorCode.message() + ", " + additionalInfo);
        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
