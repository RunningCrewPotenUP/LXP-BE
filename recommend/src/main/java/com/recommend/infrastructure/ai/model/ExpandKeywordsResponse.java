package com.recommend.infrastructure.ai.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gemini JSON 응답 파싱용 DTO
 *
 * 예시:
 * {
 *   "인프라/배포": ["Docker", "Kubernetes"],
 *   "모니터링/관측성": ["Prometheus", "Grafana"],
 *   "클라우드 플랫폼": ["AWS", "GCP"]
 * }
 */
public class ExpandKeywordsResponse {

    private final Map<String, List<String>> categories = new HashMap<>();

    /**
     * Jackson이 동적 필드를 처리하기 위한 메서드
     * 카테고리명이 고정되지 않았으므로 모든 필드 수용
     */
    @JsonAnySetter
    public void addCategory(String categoryName, List<String> keywords) {
        categories.put(categoryName, keywords);
    }

    public Map<String, List<String>> getCategories() {
        return categories;
    }

    /**
     * 모든 카테고리의 키워드를 평탄화하여 하나의 리스트로 반환
     */
    public List<String> getAllKeywords() {
        List<String> allKeywords = new ArrayList<>();
        categories.values().forEach(allKeywords::addAll);
        return allKeywords;
    }

    /**
     * 응답 유효성 검증
     */
    public boolean isValid() {
        return !categories.isEmpty() &&
                categories.values().stream()
                        .anyMatch(keywords -> keywords != null && !keywords.isEmpty());
    }
}
