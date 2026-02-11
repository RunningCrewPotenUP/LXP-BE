package com.recommend.application.dto;

import java.util.Set;

/**
 * 강좌 메타 데이터 (Recommend BC 관점)
 *
 * 추천 점수 계산 + 응답 생성에 필요한 정보
 */
public record CourseMetaData(
        Long courseId,
        String title,         // ✅ 추가 (Web Response용)
        Set<String> tags,
        String difficulty,
        boolean isPublic
) {
    public CourseMetaData {
        tags = tags != null ? Set.copyOf(tags) : Set.of();
    }
}
