package com.recommend.domain.policy;

import com.recommend.application.dto.ExpandedKeywords;
import com.recommend.application.dto.PersonaContext;
import com.recommend.application.port.required.KnowledgeExpansionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
//(Tier 2 점수 계산)
/**
 * Tier 2: AI 기반 기술 스택 확장 추천 전략
 * LangChain4j + Gemini를 활용한 의미론적 추천
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LangChainExpansionStrategy {

    private final KnowledgeExpansionPort knowledgeExpansionPort;

    @Value("${recommend.scoring.tier2-weight:0.7}")
    private double tier2Weight;

    /**
     * AI 확장 키워드 기반 점수 계산
     *
     * @param courseTags 강좌 태그
     * @param context 페르소나 컨텍스트
     * @return 0.0 ~ 1.0 점수 (Tier 1보다 낮게 설정)
     */
    public double calculateScore(Set<String> courseTags, PersonaContext context) {
        try {
            // 1. AI 확장 키워드 조회
            ExpandedKeywords expandedKeywords = knowledgeExpansionPort.expandTechStack(context);

            // 2. AI 실패 시 0점 반환
            if (expandedKeywords.isEmpty()) {
                log.debug("[Tier2] No expanded keywords, returning 0 score");
                return 0.0;
            }

            // 3. 강좌 태그와 확장 키워드 매칭
            int matchCount = 0;
            for (String courseTag : courseTags) {
                for (String expandedKeyword : expandedKeywords.keywords()) {
                    if (isMatch(courseTag, expandedKeyword)) {
                        matchCount++;
                        break;
                    }
                }
            }

            // 4. 점수 계산 (정규화)
            if (matchCount == 0) {
                return 0.0;
            }

            // 매칭된 개수에 비례, 최대 1.0
            double rawScore = Math.min(matchCount * 0.3, 1.0);
            double finalScore = rawScore * tier2Weight;

            log.debug("[Tier2] Matched {} keywords, score: {}", matchCount, finalScore);
            return finalScore;

        } catch (Exception e) {
            log.error("[Tier2] Failed to calculate score", e);
            return 0.0;
        }
    }

    /**
     * 태그 매칭 로직 (대소문자 무시, 부분 일치 포함)
     */
    private boolean isMatch(String courseTag, String expandedKeyword) {
        String normalizedCourseTag = courseTag.toLowerCase().trim();
        String normalizedKeyword = expandedKeyword.toLowerCase().trim();

        return normalizedCourseTag.equals(normalizedKeyword) ||
                normalizedCourseTag.contains(normalizedKeyword) ||
                normalizedKeyword.contains(normalizedCourseTag);
    }
}
