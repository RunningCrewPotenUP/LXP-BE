package com.lxpbe.user.domain.exception;

import com.lxpbe.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public enum UserErrorCode implements ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USR_001", "이메일이 중복됩니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USR_002", "이메일 형식이 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USR_003", "사용자를 찾을 수 없습니다."),
    INVALID_TAG_COUNT(HttpStatus.BAD_REQUEST, "USR_004", "태그는 최소 3개, 최대 5개여야 합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String code, String message) {
        Objects.requireNonNull(httpStatus, "UserErrorCode.httpStatus 는 null 일 수 없습니다.");
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
