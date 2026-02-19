package com.recommend.application.dto;

import java.util.Set;

/**
 * 학습자 프로필 데이터
 */
public record LearnerProfileData(
        Long learnerId,           //  String → Long
        Set<String> explicitTags, // 명시적 관심 태그
        Set<String> implicitTags, // 암묵적 관심 태그 (수강 중인 강좌의 태그)
        String level              // "JUNIOR", "MIDDLE", "SENIOR", "EXPERT"
) {
    public LearnerProfileData {
        explicitTags = explicitTags != null ? Set.copyOf(explicitTags) : Set.of();
        implicitTags = implicitTags != null ? Set.copyOf(implicitTags) : Set.of();
    }
}
