package com.recommend.application.port.required;

import com.recommend.application.dto.ExpandedKeywords;
import com.recommend.application.dto.PersonaContext;

/**
 * 기술 스택 확장 키워드 생성 Port (Outbound)
 * Infrastructure AI Layer에서 구현
 */
public interface KnowledgeExpansionPort {

    /**
     * 사용자 페르소나 기반 확장 키워드 생성
     *
     * @param context 페르소나 컨텍스트 (태그, 레벨, 페르소나)
     * @return AI가 생성한 확장 키워드 리스트
     */
    ExpandedKeywords expandTechStack(PersonaContext context);
}
