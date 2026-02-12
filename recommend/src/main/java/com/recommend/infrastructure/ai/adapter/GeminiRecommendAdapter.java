package com.recommend.infrastructure.ai.adapter;

import com.recommend.application.dto.ExpandedKeywords;
import com.recommend.application.dto.PersonaContext;
import com.recommend.application.port.required.KnowledgeExpansionPort;
import com.recommend.infrastructure.ai.model.ExpandKeywordsResponse;
import com.recommend.infrastructure.ai.prompt.PromptTemplateManager;
import com.recommend.infrastructure.ai.service.KnowledgeExpanderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * KnowledgeExpansionPort 구현체
 * Application Layer와 AI Infrastructure 연결
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiRecommendAdapter implements KnowledgeExpansionPort {

    private final KnowledgeExpanderService knowledgeExpanderService;
    private final PromptTemplateManager promptTemplateManager;

    @Override
    public ExpandedKeywords expandTechStack(PersonaContext context) {
        try {
            log.debug("[AI] Starting knowledge expansion for persona={}, tags={}",
                    context.persona(), context.getUserTagsAsString());

            // 1. 프롬프트 템플릿 가져오기
            String promptTemplate = promptTemplateManager.getTemplate(context.persona());

            // 2. Gemini API 호출
            ExpandKeywordsResponse response = knowledgeExpanderService.expand(
                    context.getUserTagsAsString(),
                    context.level(),
                    context.getPersonaDescription(),
                    promptTemplate
            );

            // 3. 응답 검증
            if (response == null || !response.isValid()) {
                log.warn("[AI] Invalid response from Gemini for persona={}", context.persona());
                return ExpandedKeywords.empty();
            }

            // 4. 키워드 추출 및 중복 제거
            List<String> keywords = response.getAllKeywords().stream()
                    .filter(keyword -> !context.userTags().contains(keyword))  // 사용자 태그 제외
                    .distinct()
                    .toList();

            log.info("[AI] Expanded {} keywords for persona={}: {}",
                    keywords.size(), context.persona(), keywords);

            return new ExpandedKeywords(keywords);

        } catch (Exception e) {
            log.error("[AI] Failed to expand tech stack for persona={}", context.persona(), e);
            return ExpandedKeywords.empty();
        }
    }
}
