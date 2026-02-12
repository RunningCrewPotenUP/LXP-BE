package com.recommend.infrastructure.ai.service;

import com.recommend.infrastructure.ai.model.ExpandKeywordsResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.springframework.stereotype.Service;

/**
 * LangChain4j Declarative AI Service
 * 인터페이스만 정의하면 LangChain4j가 자동으로 구현체 생성
 */
@Service
public interface KnowledgeExpanderService {

    @SystemMessage("""
        당신은 IT 학습 경로 추천 전문가입니다.
        개발자가 실무 역량을 키우기 위해 필요한 기술을 추천합니다.
        응답은 간결하고 구체적이어야 하며, JSON 형식을 엄격히 준수해야 합니다.
        사용자가 이미 학습 중인 기술과 중복되지 않는 키워드만 제시하세요.
        """)
    @UserMessage("""
        사용자는 현재 {{userTags}}를 학습 중인 {{level}} 레벨의 {{personaDescription}} 입니다.
        
        {{promptTemplate}}
        
        응답 형식: JSON (다른 텍스트 없이 JSON만 출력)
        """)
    ExpandKeywordsResponse expand(
            @V("userTags") String userTags,
            @V("level") String level,
            @V("personaDescription") String personaDescription,
            @V("promptTemplate") String promptTemplate
    );
}
