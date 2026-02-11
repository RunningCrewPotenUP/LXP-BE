package com.lxpbe.course.presentation.docs;

public final class CourseApiResponseExamples {

    private CourseApiResponseExamples() {
    }

    public static final String SEARCH_200 = """
            {
                "data": {
                    "content": [
                        {
                            "id": 1,
                            "title": "Spring Boot 입문",
                            "description": "Spring Boot 기초 강좌",
                            "thumbnailUrl": "https://example.com/thumbnail.jpg",
                            "level": { "key": "JUNIOR", "value": "주니어" },
                            "instructor": { "instructorId": 1, "name": "홍길동" },
                            "tags": [{ "id": 1, "content": "Spring" }],
                            "createdAt": "2025-01-01T00:00:00",
                            "updatedAt": "2025-01-01T00:00:00"
                        }
                    ],
                    "totalElements": 1,
                    "totalPages": 1,
                    "number": 0,
                    "size": 20
                },
                "error": null
            }
            """;

    public static final String DETAIL_200 = """
            {
                "data": {
                    "id": 1,
                    "title": "Spring Boot 입문",
                    "description": "Spring Boot 기초 강좌",
                    "thumbnailUrl": "https://example.com/thumbnail.jpg",
                    "level": { "key": "JUNIOR", "value": "주니어" },
                    "instructor": { "instructorId": 1, "name": "홍길동" },
                    "tags": [{ "id": 1, "content": "Spring" }],
                    "durationInHours": 10,
                    "sections": [
                        {
                            "id": 1,
                            "title": "1장. 시작하기",
                            "durationInSeconds": 3600,
                            "order": 1,
                            "lectures": [
                                {
                                    "id": 1,
                                    "title": "강의 소개",
                                    "videoUrl": "https://example.com/video.mp4",
                                    "order": 1,
                                    "durationInSeconds": 1800
                                }
                            ]
                        }
                    ],
                    "createdAt": "2025-01-01T00:00:00",
                    "updatedAt": "2025-01-01T00:00:00"
                },
                "error": null
            }
            """;

    public static final String CREATE_400 = """
            {
                "data": null,
                "error": {
                    "code": "INVALID_ARGUMENT",
                    "message": "유효하지 않은 인자(공통 예외)(title: 강좌 제목은 필수입니다)"
                }
            }
            """;

    public static final String INVALID_INSTRUCTOR_400 = """
            {
                "data": null,
                "error": {
                    "code": "CRS_006",
                    "message": "강사만 강좌를 생성할 수 있습니다"
                }
            }
            """;

    public static final String NOT_FOUND_404 = """
            {
                "data": null,
                "error": {
                    "code": "CRS_001",
                    "message": "강좌를 찾을 수 없습니다"
                }
            }
            """;

    public static final String UPDATE_DENIED_403 = """
            {
                "data": null,
                "error": {
                    "code": "CRS_007",
                    "message": "강좌를 생성한 강사만 강좌를 수정할 수 있습니다"
                }
            }
            """;

    public static final String DELETE_DENIED_403 = """
            {
                "data": null,
                "error": {
                    "code": "CRS_008",
                    "message": "강좌를 생성한 강사만 삭제할 수 있습니다"
                }
            }
            """;
}
