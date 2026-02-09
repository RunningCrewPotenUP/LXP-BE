package com.lxpbe.course.exception;

import com.lxpbe.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public enum CourseErrorCode implements ErrorCode {

    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_NOT_FOUND", "강좌를 찾을 수 없습니다"),
    INVALID_COURSE_DATA(HttpStatus.BAD_REQUEST, "INVALID_COURSE_DATA", "유효하지 않은 강좌 데이터입니다"),
    INVALID_LEVEL(HttpStatus.BAD_REQUEST, "INVALID_LEVEL", "유효하지 않은 난이도입니다"),
    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "SECTION_NOT_FOUND", "섹션을 찾을 수 없습니다"),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "LECTURE_NOT_FOUND", "강의를 찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CourseErrorCode(HttpStatus httpStatus, String code, String message) {
        Objects.requireNonNull(httpStatus, "CourseErrorCode.httpStatus는 null일 수 없습니다.");
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
