package com.recommend.application.dto;

import com.recommend.domain.model.PersonaType;
import java.util.Set;

/**
 * AI 프롬프트 생성을 위한 페르소나 컨텍스트
 */
public record PersonaContext(
        PersonaType persona,
        Set<String> userTags,
        String level
) {

    /**
     * 태그를 쉼표로 구분된 문자열로 변환
     */
    public String getUserTagsAsString() {
        return String.join(", ", userTags);
    }

    /**
     * 페르소나 한글 설명 반환
     */
    public String getPersonaDescription() {
        return persona.getDescription();
    }
}
