package com.recommend.application.dto;

import java.util.List;

/**
 * AI가 생성한 확장 키워드 응답
 */
public record ExpandedKeywords(
        List<String> keywords
) {

    /**
     * 빈 응답 생성 (Fallback용)
     */
    public static ExpandedKeywords empty() {
        return new ExpandedKeywords(List.of());
    }

    /**
     * 키워드 존재 여부
     */
    public boolean isEmpty() {
        return keywords == null || keywords.isEmpty();
    }

    /**
     * 키워드 개수
     */
    public int size() {
        return keywords != null ? keywords.size() : 0;
    }
}
