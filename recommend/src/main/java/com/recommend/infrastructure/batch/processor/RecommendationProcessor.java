package com.recommend.infrastructure.batch.processor;

import com.recommend.application.service.RecommendCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 학습자별 추천 계산 Processor
 * Reader에서 받은 학습자 ID로 추천 계산 수행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationProcessor implements ItemProcessor<Long, Long> {  // ✅ Long으로 변경

    private final RecommendCommandService commandService;

    /**
     * 학습자 ID를 받아 추천 계산 수행
     *
     * @param learnerId 학습자 ID (Long)
     * @return 성공 시 learnerId, 실패 시 null (Writer로 전달 안 됨)
     */
    @Override
    public Long process(Long learnerId) throws Exception {  // ✅ Long으로 변경
        log.debug("[Batch Processor] Processing recommendation for learnerId={}", learnerId);

        try {
            // 기존 추천 계산 로직 재사용
            commandService.refreshRecommendations(learnerId);  // ✅ Long 파라미터

            log.debug("[Batch Processor] Successfully processed learnerId={}", learnerId);
            return learnerId;  // ✅ 성공한 learnerId 반환

        } catch (Exception e) {
            log.error("[Batch Processor] Failed to process learnerId={}", learnerId, e);
            // ✅ null 반환 시 Writer로 전달되지 않음 (스킵 처리)
            return null;
        }
    }
}
