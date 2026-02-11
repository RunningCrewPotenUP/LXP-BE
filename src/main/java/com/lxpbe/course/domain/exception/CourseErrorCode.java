package com.lxpbe.course.domain.exception;

import com.lxpbe.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public enum CourseErrorCode implements ErrorCode {

    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "CRS_001", "강좌를 찾을 수 없습니다"),
    INVALID_COURSE_DATA(HttpStatus.BAD_REQUEST, "CRS_002", "유효하지 않은 강좌 데이터입니다"),
    INVALID_LEVEL(HttpStatus.BAD_REQUEST, "CRS_003", "유효하지 않은 난이도입니다"),
    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "CRS_004", "섹션을 찾을 수 없습니다"),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "CRS_005", "강의를 찾을 수 없습니다"),
    INVALID_INSTRUCTOR(HttpStatus.BAD_REQUEST, "CRS_006", "강사만 강좌를 생성할 수 있습니다"),
    COURSE_UPDATE_DENIED(HttpStatus.FORBIDDEN, "CRS_007", " 강좌를 생성한 강사만 강좌를 수정할 수 있습니다"),
    COURSE_DELETE_DENIED(HttpStatus.FORBIDDEN, "CRS_008", "강좌를 생성한 강사만 삭제할 수 있습니다"),
    INSTRUCTOR_ID_IS_REQUIRED_TO_CREATE_COURSE(HttpStatus.BAD_REQUEST, "CRS_009", "강사 id가 null이어서 강좌를 생성할 수 없습니다"),
    TITLE_IS_REQUIRED_TO_CREATE_COURSE(HttpStatus.BAD_REQUEST, "CRS_010", "강좌 이름은 필수 값입니다"),
    TAG_IS_REQUIRED_TO_CREATE_COURSE(HttpStatus.BAD_REQUEST, "CRS_011", "강좌 생성시 태그가 1개 이상 등록되어야 합니다"),
    TAG_IS_REQUIRED_TO_UPDATE_COURSE(HttpStatus.BAD_REQUEST, "CRS_012", "강좌 업데이트시 태그 1개 이상 등록되어야 합니다")
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
