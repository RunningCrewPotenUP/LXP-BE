package com.recommend.infrastructure.ai.model;

/**
 * Gemini API 요청용 DTO
 * PersonaContext를 LLM 프롬프트 변수로 변환
 */
public record ExpandKeywordsRequest(
        String userTags,
        String level,
        String persona
) {

    /**
     * PersonaContext로부터 생성
     */
    public static ExpandKeywordsRequest from(
            String userTags,
            String level,
            String persona
    ) {
        return new ExpandKeywordsRequest(userTags, level, persona);
    }
}
