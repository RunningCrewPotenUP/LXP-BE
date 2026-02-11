package com.lxpbe.auth.presentation.docs;

public final class AuthApiResponseExamples {

    private AuthApiResponseExamples() {
    }

    public static final String REGISTER_400 = """
            {
                "data": null,
                "error": {
                    "code": "INVALID_ARGUMENT",
                    "message": "유효하지 않은 인자(공통 예외)(email: 올바른 이메일 형식이 아닙니다.)"
                }
            }
            """;

    public static final String REGISTER_409 = """
            {
                "data": null,
                "error": {
                    "code": "USR_001",
                    "message": "이메일이 중복됩니다."
                }
            }
            """;

    public static final String LOGIN_401 = """
            {
                "data": null,
                "error": {
                    "code": "AUTH_001",
                    "message": "이메일이나 비밀번호가 일치하지 않습니다."
                }
            }
            """;
}
