package com.lxpbe.user.presentation.docs;

public final class UserApiResponseExamples {

    private UserApiResponseExamples() {
    }

    public static final String MY_INFO_200 = """
            {
                "data": {
                    "userId": 1,
                    "email": "user@example.com",
                    "name": "홍길동",
                    "roles": ["LEARNER"],
                    "tags": [
                        { "id": 1, "content": "LLM" },
                        { "id": 2, "content": "ChatGPT" },
                        { "id": 3, "content": "StableDiffusion" }
                    ],
                    "level": "JUNIOR"
                },
                "error": null
            }
            """;

    public static final String UPDATE_400 = """
            {
                "data": null,
                "error": {
                    "code": "USR_004",
                    "message": "태그는 최소 3개, 최대 5개여야 합니다."
                }
            }
            """;
}
