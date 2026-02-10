package com.recommend.application.service;

import com.recommend.application.dto.CourseMetaData;
import com.recommend.application.dto.LearnerProfileData;
import com.recommend.application.dto.LearningHistoryData;
import com.recommend.application.port.provided.persistence.MemberRecommendationRepository;
import com.recommend.application.port.required.CourseMetaQueryPort;
import com.recommend.application.port.required.LearnerProfileQueryPort;
import com.recommend.application.port.required.LearningHistoryQueryPort;
import com.recommend.domain.model.*;
import com.recommend.domain.model.ids.CourseId;
import com.recommend.domain.model.ids.EnrollmentStatus;
import com.recommend.domain.model.ids.Level;
import com.recommend.domain.model.ids.MemberId;
import com.recommend.domain.policy.ScoringPolicy;
// ❌ 제거: import com.recommend.infrastructure.external.common.LevelMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendCommandService {

    private final MemberRecommendationRepository recommendationRepository;
    private final LearnerProfileQueryPort userPort;
    private final CourseMetaQueryPort coursePort;
    private final LearningHistoryQueryPort historyPort;

    /**
     * 추천 재계산
     */
    @Transactional
    public void refreshRecommendations(String learnerId) {
        log.info("[추천 재계산 시작] learnerId={}", learnerId);

        try {
            // 1. Context 수집
            RecommendContext context = assembleContext(learnerId);

            // 2. 점수 계산
            List<RecommendedCourse> courses = calculateScores(context);

            // 3. Aggregate 저장
            MemberId memberId = new MemberId(learnerId);
            MemberRecommendation recommendation = findOrCreateRecommendation(memberId);
            recommendation.updateRecommendations(courses);

            recommendationRepository.save(recommendation);

            log.info("[추천 재계산 완료] learnerId={}, 추천개수={}", learnerId, courses.size());

        } catch (Exception e) {
            log.error("[추천 재계산 실패] learnerId={}", learnerId, e);
            throw new RuntimeException("추천 재계산 중 오류 발생", e);
        }
    }

    /**
     * Context 조립
     */
    private RecommendContext assembleContext(String learnerId) {
        // 1. 학습자 프로필
        LearnerProfileData profile = userPort.getProfile(learnerId)
                .orElseThrow(() -> new IllegalArgumentException("학습자를 찾을 수 없습니다: " + learnerId));

        // 2. 학습 이력
        List<LearningHistoryData> historyData = historyPort.findByLearnerId(learnerId);
        List<LearningHistory> histories = historyData.stream()
                .map(h -> new LearningHistory(
                        new CourseId(h.courseId()),
                        EnrollmentStatus.valueOf(h.status())
                ))
                .toList();

        // 3. 강좌 후보
        List<CourseMetaData> courseMetas = coursePort.findAll();
        List<CourseCandidate> candidates = courseMetas.stream()
                .map(meta -> new CourseCandidate(
                        new CourseId(meta.courseId()),
                        new HashSet<>(meta.tags()),
                        Level.fromString(meta.difficulty()),  // ✅ Level에 정적 메서드 사용
                        meta.isPublic()
                ))
                .toList();

        // 4. Context 생성
        return RecommendContext.builder()
                .explicitTags(new HashSet<>(profile.explicitTags()))
                .implicitTags(new HashSet<>(profile.implicitTags()))
                .learnerLevel(profile.level())
                .learningHistories(histories)
                .courseCandidates(candidates)
                .build();
    }

    /**
     * 점수 계산
     */
    private List<RecommendedCourse> calculateScores(RecommendContext context) {
        ScoringPolicy policy = new ScoringPolicy();

        // 필터링된 후보만 계산
        List<CourseCandidate> filtered = context.getFilteredCandidates();

        List<ScoredItem> scored = filtered.stream()
                .map(candidate -> {
                    double score = policy.calculateScore(
                            candidate.getTags(),
                            context.getExplicitTags(),
                            context.getImplicitTags()
                    );
                    return new ScoredItem(candidate.getCourseId(), score);
                })
                .sorted(Comparator.comparingDouble(ScoredItem::score).reversed())
                .limit(10)  // 최대 10개
                .toList();

        // RecommendedCourse 변환
        List<RecommendedCourse> result = new ArrayList<>();
        for (int i = 0; i < scored.size(); i++) {
            ScoredItem item = scored.get(i);
            result.add(new RecommendedCourse(item.courseId, item.score, i + 1));
        }

        return result;
    }

    private record ScoredItem(CourseId courseId, double score) {}

    private MemberRecommendation findOrCreateRecommendation(MemberId memberId) {
        return recommendationRepository.findByMemberId(memberId)
                .orElseGet(() -> new MemberRecommendation(memberId));
    }
}
