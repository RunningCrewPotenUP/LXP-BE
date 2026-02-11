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
            				"courseId": 1,\s
            				"status": "ENROLLED",  // ENROLLED, IN_PROGRESS, COMPLETED, CANCELLED
            				"enrolledAt": "2025-12-11 12:48:22+09:00",  // enrolledAt 을 제외한 날짜은 null 일 수 있어요
            				"learningStartedAt": "...",
            
            				"cancelledAt": "...",
            				"cancelType": "SELF_SERVICE",
            				"reasonType": "POOR_QUALITY",
            				"reason": "강사님이 대머리여서 너무 눈부셔요"
            
            				"instructorId": 1,
            	      "instructorName": "강사1",
            
            	      "thumbnailUrl": null,
            	      "totalProgress": 0.0,
            	      "courseTitle": "Java 기초 1",
            	      "courseDescription": "주니어를 위한 자바 입문",
            	      "courseLevel": "JUNIOR",
            	      "tags": [
                      {
            					    "id": 83,
            			        "category": "서비스",
            			        "subCategory": "마케팅/운영",
            			        "name": "그로스해킹",
            			    },
            			    {
            					    "id": 90,
            			        "category": "서비스",
            			        "subCategory": "법률/경영",
            			        "name": "IT법률",
            			    },
            			    {
            					    "id": 93,
            			        "category": "서비스",
            			        "subCategory": "기타",
            			        "name": "채용/면접",
            			    },
            	      ]
            	    },
            	    {
            	      ...
            	    }
            		],  // end of content
            	  "pageNumber": 0,
            	  "pageSize": 10,
            	  "totalElements": 219,
            	  "totalPages": 22,
            	},  // end of data
            	"error": null,
            }
            """;

    public static final String QUERY_DETAILS_200 = """
            {
                "data": {
                    "id": 1,
                    "courseId": 1,
                    "status": "ENROLLED",  // ENROLLED, IN_PROGRESS, COMPLETED, CANCELLED
                    "enrolledAt": "2025-12-11 12:48:22+09:00",  // enrolledAt 을 제외한 날짜은 null 일 수 있어요
                    "learningStartedAt": "...",
    
                    "cancelledAt": "...",
                    "cancelType": "SELF_SERVICE",
                    "reasonType": "POOR_QUALITY",
                    "reason": "강사님이 대머리여서 너무 눈부셔요"
    
                    "instructorId": 1,
                    "instructorName": "강사1",
        
                    "thumbnailUrl": null,
                    "totalProgress": 0.0,
                    "courseTitle": "Java 기초 1",
                    "courseDescription": "주니어를 위한 자바 입문",
                    "courseLevel": "JUNIOR",
                    "tags": [
                        {
                                "id": 83,
                            "category": "서비스",
                            "subCategory": "마케팅/운영",
                            "name": "그로스해킹",
                        },
                        {
                                "id": 90,
                            "category": "서비스",
                            "subCategory": "법률/경영",
                            "name": "IT법률",
                        },
                        {
                                "id": 93,
                            "category": "서비스",
                            "subCategory": "기타",
                            "name": "채용/면접",
                        },
                    ]
                }
                "error": null
            }
            """;
}
