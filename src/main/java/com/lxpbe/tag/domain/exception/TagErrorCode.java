package com.lxpbe.tag.domain.exception;

import com.lxpbe.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public enum TagErrorCode implements ErrorCode {

    TAG_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "TG_0001",
            "태그를 찾을 수 없습니다."
    ),
    ;

    // -----

    private HttpStatus httpStatus;
    private String code;
    private String message;

    TagErrorCode(HttpStatus httpStatus, String code, String message) {
        Objects.requireNonNull(httpStatus, "TagErrorCode.httpStatus 는 null 일 수 없습니다.");
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
