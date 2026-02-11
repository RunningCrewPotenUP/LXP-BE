package com.recommend.infrastructure.web.dto.response;

import java.util.Set;

/**
 * 추천 강좌 응답 DTO
 */
public record RecommendedCourseResponse(
        CourseInfo course,  // ✅ 내장 DTO (CourseMetaResponse 대체)
        double score,
        int rank
) {
    /**
     * 강좌 정보 (내장)
     */
    public record CourseInfo(
            Long courseId,
            String title,           // ← Adapter에서 조회 필요
            Set<String> tags,
            String difficulty,
            boolean isPublic
    ) {}
}
