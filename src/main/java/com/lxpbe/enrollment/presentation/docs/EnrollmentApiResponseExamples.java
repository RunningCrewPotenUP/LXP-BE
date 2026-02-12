package com.lxpbe.enrollment.presentation.docs;

public final class EnrollmentApiResponseExamples {

    private EnrollmentApiResponseExamples() {
    }

    public static final String ENROLL_201 = """
            {
                "data": {
                    "id": 1,
                    "courseId": 10,
                    "status": "ENROLLED",
                    "enrolledAt": "2025-01-01T00:00:00Z"
                },
                "error": null
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
                "data": {
                    "id": 1,
                    "courseId": 10,
                    "status": "CANCELLED",
                    "enrolledAt": "2025-01-01T00:00:00Z",
                    "learningStartedAt": null,
                    "cancelledAt": "2025-01-02T00:00:00Z",
                    "cancelType": "USER",
                    "reasonType": "PURCHASE_MISTAKE",
                    "reason": "착오로 인한 취소입니다."
                },
                "error": null
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

    public static final String QUERY_SUMMARIES_200 = """
            {
                "data": {
                    "content": [
                        {
                            "id": 1,
                            "courseId": 1,
                            "status": "ENROLLED",
                            "enrolledAt": "2026-02-12T06:56:08.517295Z",
                            "learningStartedAt": null,
                            "cancelledAt": null,
                            "cancelType": null,
                            "reasonType": null,
                            "reason": null,
                            "instructorId": 1,
                            "instructorName": "김길동",
                            "thumbnailUrl": "https://example.com/thumbnail.jpg",
                            "courseTitle": "Spring Boot 완전 정복",
                            "courseDescription": "Spring Boot를 활용한 백엔드 개발 마스터 과정",
                            "courseLevel": "JUNIOR",
                            "tags": [
                                {
                                    "tagId": 1,
                                    "name": "LLM",
                                    "category": "AI",
                                    "subCategory": "Generative AI"
                                },
                                {
                                    "tagId": 2,
                                    "name": "ChatGPT",
                                    "category": "AI",
                                    "subCategory": "Generative AI"
                                },
                                {
                                    "tagId": 3,
                                    "name": "StableDiffusion",
                                    "category": "AI",
                                    "subCategory": "Generative AI"
                                },
                                {
                                    "tagId": 4,
                                    "name": "Midjourney",
                                    "category": "AI",
                                    "subCategory": "Generative AI"
                                }
                            ],
                            "totalProgress": 0.0
                        },
                        {
                            "id": 2,
                            "courseId": 2,
                            "status": "ENROLLED",
                            "enrolledAt": "2026-02-12T08:55:28.504534Z",
                            "learningStartedAt": null,
                            "cancelledAt": null,
                            "cancelType": null,
                            "reasonType": null,
                            "reason": null,
                            "instructorId": 1,
                            "instructorName": "김길동",
                            "thumbnailUrl": "https://example.com/thumbnail.jpg",
                            "courseTitle": "자바 완전 정복",
                            "courseDescription": "자바 21 본격 해부!",
                            "courseLevel": "JUNIOR",
                            "tags": [
                                {
                                    "tagId": 5,
                                    "name": "GenerativeAI",
                                    "category": "AI",
                                    "subCategory": "Generative AI"
                                },
                                {
                                    "tagId": 6,
                                    "name": "TensorFlow",
                                    "category": "AI",
                                    "subCategory": "머신러닝/딥러닝"
                                }
                            ],
                            "totalProgress": 0.0
                        }
                    ],
                    "pageable": {
                        "pageNumber": 0,
                        "pageSize": 10,
                        "sort": {
                            "empty": false,
                            "sorted": true,
                            "unsorted": false
                        },
                        "offset": 0,
                        "paged": true,
                        "unpaged": false
                    },
                    "last": true,
                    "totalElements": 2,
                    "totalPages": 1,
                    "size": 10,
                    "number": 0,
                    "sort": {
                        "empty": false,
                        "sorted": true,
                        "unsorted": false
                    },
                    "first": true,
                    "numberOfElements": 2,
                    "empty": false
                },
                "error": null
            }
            """;

    public static final String QUERY_DETAILS_200 = """
            {
                "data": {
                    "id": 1,
                    "courseId": 1,
                    "status": "ENROLLED",
                    "enrolledAt": "2026-02-12T06:56:08.517295Z",
                    "learningStartedAt": null,
                    "cancelledAt": null,
                    "cancelType": null,
                    "reasonType": null,
                    "reason": null,
                    "instructorId": 1,
                    "instructorName": "김길동",
                    "thumbnailUrl": "https://example.com/thumbnail.jpg",
                    "courseTitle": "Spring Boot 완전 정복",
                    "courseDescription": "Spring Boot를 활용한 백엔드 개발 마스터 과정",
                    "courseLevel": "JUNIOR",
                    "tags": [
                        {
                            "tagId": 1,
                            "name": "LLM",
                            "category": "AI",
                            "subCategory": "Generative AI"
                        },
                        {
                            "tagId": 2,
                            "name": "ChatGPT",
                            "category": "AI",
                            "subCategory": "Generative AI"
                        },
                        {
                            "tagId": 3,
                            "name": "StableDiffusion",
                            "category": "AI",
                            "subCategory": "Generative AI"
                        },
                        {
                            "tagId": 4,
                            "name": "Midjourney",
                            "category": "AI",
                            "subCategory": "Generative AI"
                        }
                    ],
                    "totalProgress": 0.0
                },
                "error": null
            }
            """;
}
