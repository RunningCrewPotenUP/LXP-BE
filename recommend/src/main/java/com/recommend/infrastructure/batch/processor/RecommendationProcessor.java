package com.recommend.infrastructure.batch.processor;

import com.recommend.application.service.RecommendCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 학습자별 추천 계산 Processor (2-Tier AI 통합)
 * Reader에서 받은 학습자 ID로 추천 계산 수행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationProcessor implements ItemProcessor<Long, Long> {

    private final RecommendCommandService commandService;

    /**
     * 학습자 ID를 받아 2-Tier 추천 계산 수행
     *
     * @param learnerId 학습자 ID (Long)
     * @return 성공 시 learnerId, 실패 시 null (Writer로 전달 안 됨)
     */
    @Override
    public Long process(Long learnerId) throws Exception {
        log.debug("[Batch Processor] Processing recommendation for learnerId={}", learnerId);

        try {
            // 2-Tier 추천 계산 (Tier 1 기존 + Tier 2 AI)
            commandService.refreshRecommendations(learnerId);

            log.debug("[Batch Processor] Successfully processed learnerId={}", learnerId);
            return learnerId;

        } catch (Exception e) {
            log.error("[Batch Processor] Failed to process learnerId={}", learnerId, e);
            // null 반환 시 Writer로 전달되지 않음 (스킵 처리)
            return null;
        }
    }
}

/**
 * 배치 처리 흐름 (AI 통합)
 *
 * 1. LearnerIdReader
 *    ↓ 200명 학습자 ID 조회
 *
 * 2. RecommendationProcessor (Chunk 10개)
 *    ↓ commandService.refreshRecommendations()
 *    ↓   ├─ Tier 1: 기존 태그 매칭 (6개)
 *    ↓   ├─ Tier 2: AI 확장 추천 (4개) 🆕
 *    ↓   │   ├─ PersonaDetectionService
 *    ↓   │   ├─ KnowledgeExpansionPort (Gemini API)
 *    ↓   │   └─ LangChainExpansionStrategy
 *    ↓   └─ 통합 10개 선정
 *
 * 3. RecommendationWriter
 *    ↓ DB 저장
 *
 * 예상 시간:
 * - 200명 ÷ 10 (Chunk) = 20 Chunk
 * - Chunk당 약 5.8초 (Gemini 호출 포함)
 * - 총 약 2분 이내
 */
