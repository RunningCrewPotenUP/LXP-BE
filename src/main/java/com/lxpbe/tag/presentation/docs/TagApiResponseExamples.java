package com.lxpbe.tag.presentation.docs;

public final class TagApiResponseExamples {

    private TagApiResponseExamples() {
    }

    public static final String TAG_LIST_200 = """
            [
                {
                    "tagId": 1,
                    "name": "Spring",
                    "category": "FRAMEWORK",
                    "subCategory": "BACKEND",
                    "status": "ACTIVE"
                },
                {
                    "tagId": 2,
                    "name": "React",
                    "category": "FRAMEWORK",
                    "subCategory": "FRONTEND",
                    "status": "ACTIVE"
                }
            ]
            """;

    public static final String TAG_200 = """
            {
                "tagId": 1,
                "name": "Spring",
                "category": "FRAMEWORK",
                "subCategory": "BACKEND",
                "status": "ACTIVE"
            }
            """;

    public static final String TAG_NOT_FOUND_404 = """
            {
                "data": null,
                "error": {
                    "code": "TG_0001",
                    "message": "태그를 찾을 수 없습니다."
                }
            }
            """;
}
