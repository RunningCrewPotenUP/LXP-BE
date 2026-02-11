package com.lxpbe.enrollment.presentation.docs;

public final class EnrollmentApiResponseExamples {

    private EnrollmentApiResponseExamples() {
    }

    public static final String ENROLL_201 = """
            {
                "id": 1,
                "courseId": 10,
                "status": "ENROLLED",
                "enrolledAt": "2025-01-01T00:00:00Z"
            }
            """;

    public static final String ENROLL_400 = """
            {
                "data": null,
                "error": {
                    "code": "INVALID_ARGUMENT",
                    "message": "유효하지 않은 인자(공통 예외)(courseId: courseId 는 필수입니다.)"
                }
            }
            """;

    public static final String ENROLL_409 = """
            {
                "data": null,
                "error": {
                    "code": "ENRL_0013",
                    "message": "사용자의 해당 강좌에 대한 취소되지 않은 수강 건이 이미 존재합니다."
                }
            }
            """;

    public static final String CANCEL_200 = """
            {
                "id": 1,
                "courseId": 10,
                "status": "CANCELLED",
                "enrolledAt": "2025-01-01T00:00:00Z",
                "learningStartedAt": null,
                "cancelledAt": "2025-01-02T00:00:00Z",
                "cancelType": "USER",
                "reasonType": "PURCHASE_MISTAKE",
                "reason": "착오로 인한 취소입니다."
            }
            """;

    public static final String CANCEL_400 = """
            {
                "data": null,
                "error": {
                    "code": "INVALID_ARGUMENT",
                    "message": "유효하지 않은 인자(공통 예외)(enrollmentId: enrollmentId 는 필수입니다.)"
                }
            }
            """;

    public static final String CANCEL_404 = """
            {
                "data": null,
                "error": {
                    "code": "ENRL_0014",
                    "message": "수강을 찾을 수 없습니다."
                }
            }
            """;

    public static final String CANCEL_FORBIDDEN_403 = """
            {
                "data": null,
                "error": {
                    "code": "ENRL_0015",
                    "message": "다른 사람의 수강을 취소할 수 없습니다."
                }
            }
            """;
}
