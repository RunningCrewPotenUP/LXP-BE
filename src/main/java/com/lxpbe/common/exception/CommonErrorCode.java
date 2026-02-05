package com.lxpbe.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    UNEXPECTED_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR_001", "예기치 못한 서버 에러가 발생했습니다.")
    // ... 필요할 때 추가
    ;

    // -----

    private HttpStatus httpStatus;
    private String code;
    private String message;

    CommonErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String code() {
        return "";
    }

    @Override
    public String message() {
        return "";
    }
}
