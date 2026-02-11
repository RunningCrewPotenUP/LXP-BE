package com.lxpbe.resource.domain.exception;

import com.lxpbe.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public enum ResourceErrorCode implements ErrorCode {

    ;

    // -----

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ResourceErrorCode(HttpStatus httpStatus, String code, String message) {
        Objects.requireNonNull(httpStatus, "ResourceErrorCode.httpStatus 는 null 일 수 없습니다.");
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
