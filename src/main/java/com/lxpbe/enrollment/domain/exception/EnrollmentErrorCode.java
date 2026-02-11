package com.lxpbe.enrollment.domain.exception;

import com.lxpbe.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public enum EnrollmentErrorCode implements ErrorCode {

    USER_ID_IS_REQUIRED_TO_CREATE_ENROLLMENT(
            HttpStatus.BAD_REQUEST,
            "ENRL_001",
            "userId 가 null 이어서 수강을 생성할 수 없습니다."
    ),
    COURSE_ID_IS_REQUIRED_TO_CREATE_ENROLLMENT(
            HttpStatus.BAD_REQUEST,
            "ENRL_002",
            "courseId 가 null 이어서 수강을 생성할 수 없습니다."
    ),
    CANCEL_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT(
            HttpStatus.BAD_REQUEST,
            "ENRL_0003",
            "cancelType 이 null 이어서 수강을 취소할 수 없습니다."
    ),
    CANCEL_REASON_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT(
            HttpStatus.BAD_REQUEST,
            "ENRL_0004",
            "cancelReasonType 이 null 이어서 수강을 취소할 수 없습니다."
    ),
    CANCEL_REASON_COMMENT_MUST_NOT_BLANK_WHEN_CANCEL_REASON_IS_OTHER(
            HttpStatus.BAD_REQUEST,
            "ENRL_0005",
            "cancelReasonType 이 OTHER 인 경우 cancelReasonComment 는 blank 일 수 없습니다."
    ),

    ALREADY_IN_PROGRESS(
            HttpStatus.CONFLICT,
            "ENRL_0006",
            "이미 수강 시작한 상태입니다."
    ),
    INVALID_STATUS_CHANGE_INTO_IN_PROGRESS(
            HttpStatus.CONFLICT,
            "ENRL_0007",
            "수강 상태 변경 실패: IN_PROGRESS 상태로의 변경은 ENROLLED 상태에서만 가능합니다."
    ),
    ALREADY_CANCELLED(
            HttpStatus.CONFLICT,
            "ENRL_0008",
            "이미 취소된 상태입니다."
    ),
    INVALID_STATUS_CHANGE_INTO_CANCELLED(
            HttpStatus.CONFLICT,
            "ENRL_0009",
            "수강 상태 변경 실패: CANCELLED 상태로의 변경은 ENROLLED 또는 IN_PROGRESS 상태에서만 가능합니다."
    ),
    ALREADY_COMPLETED(
            HttpStatus.CONFLICT,
            "ENRL_0010",
            "이미 완료된 상태입니다."
    ),
    INVALID_STATUS_CHANGE_INTO_COMPLETED(
            HttpStatus.CONFLICT,
            "ENRL_0011",
            "수강 상태 변경 실패: COMPLETED 상태로의 변경은 IN_PROGRESS 상태에서만 가능합니다."
    ),
    DATE_ORDER_CONSISTENCY_HAS_BROKEN(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "ENRL_0012",
            "수강 주요 이벤트 발생 시각의 순서 정합성이 깨졌습니다."
    ),

    ENROLLMENT_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "ENRL_0013",
            "사용자의 해당 강좌에 대한 취소되지 않은 수강 건이 이미 존재합니다."
    ),
    ENROLLMENT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "ENRL_0014",
            "수강을 찾을 수 없습니다."
    ),
    FORBIDDEN_ENROLLMENT_CANCEL(
            HttpStatus.FORBIDDEN,
            "ENRL_0015",
            "다른 사람의 수강을 취소할 수 없습니다."
    ),
    FORBIDDEN_ENROLLMENT_QUERY(
            HttpStatus.FORBIDDEN,
            "ENRL_0015",
            "본인 또는 강사만 수강 정보를 조회할 수 있습니다."
    ),
    ;

    // -----

    private HttpStatus httpStatus;
    private String code;
    private String message;

    EnrollmentErrorCode(HttpStatus httpStatus, String code, String message) {
        Objects.requireNonNull(httpStatus, "EnrollmentErrorCode.httpStatus 는 null 일 수 없습니다.");
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
