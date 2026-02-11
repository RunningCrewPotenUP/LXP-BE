package com.recommend.application.service;

import com.recommend.application.dto.CourseMetaData;
import com.recommend.application.dto.LearnerProfileData;
import com.recommend.application.dto.LearningHistoryData;
import com.recommend.application.dto.PersonaContext;
import com.recommend.application.port.provided.persistence.MemberRecommendationRepository;
import com.recommend.application.port.required.CourseMetaQueryPort;
import com.recommend.application.port.required.LearnerProfileQueryPort;
import com.recommend.application.port.required.LearningHistoryQueryPort;
import com.recommend.domain.model.*;
import com.recommend.domain.model.ids.CourseId;
import com.recommend.domain.model.ids.EnrollmentStatus;
import com.recommend.domain.model.ids.Level;
import com.recommend.domain.model.ids.MemberId;
import com.recommend.domain.policy.LangChainExpansionStrategy;
import com.recommend.domain.policy.ScoringPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 추천 계산 서비스 (2-Tier 통합)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendCommandService {

    // Ports
    private final MemberRecommendationRepository recommendationRepository;
    private final LearnerProfileQueryPort userPort;
    private final CourseMetaQueryPort coursePort;
    private final LearningHistoryQueryPort historyPort;

    // 전략 (기존 + 신규)
    private final ScoringPolicy tier1Policy;
    private final LangChainExpansionStrategy tier2Strategy;
    private final PersonaDetectionService personaDetectionService;

    @Value("${recommend.scoring.tier1-count:6}")
    private int tier1Count;

    @Value("${recommend.scoring.tier2-count:4}")
    private int tier2Count;

    @Value("${recommend.ai.enabled:true}")
    private boolean aiEnabled;

    /**
     * 추천 재계산 (2-Tier 통합)
     */
    @Transactional
    public void refreshRecommendations(Long learnerId) {
        log.info("[추천 재계산 시작] learnerId={}", learnerId);

        try {
            // 1. Context 수집
            RecommendContext context = assembleContext(learnerId);

            // 2. Tier 1 추천 (기존 태그 매칭)
            List<RecommendedCourse> tier1Courses = calculateTier1(context);

            // 3. Tier 2 추천 (AI 확장) - AI 활성화 시에만
            List<RecommendedCourse> tier2Courses = Collections.emptyList();
            if (aiEnabled) {
                tier2Courses = calculateTier2(context, learnerId);
            } else {
                log.info("[추천] AI 비활성화, Tier 2 생략 learnerId={}", learnerId);
            }

            // 4. 통합 및 중복 제거
            List<RecommendedCourse> finalCourses = mergeTiers(tier1Courses, tier2Courses);

            // 5. Aggregate 저장
            MemberId memberId = MemberId.of(learnerId);
            MemberRecommendation recommendation = findOrCreateRecommendation(memberId);
            recommendation.updateItems(finalCourses);

            recommendationRepository.save(recommendation);

            log.info("[추천 재계산 완료] learnerId={}, 총개수={} (tier1={}, tier2={})",
                    learnerId, finalCourses.size(), tier1Courses.size(), tier2Courses.size());

        } catch (Exception e) {
            log.error("[추천 재계산 실패] learnerId={}", learnerId, e);
            throw new RuntimeException("추천 재계산 중 오류 발생", e);
        }
    }

    /**
     * Context 조립
     */
    private RecommendContext assembleContext(Long learnerId) {
        // 1. 학습자 프로필
        LearnerProfileData profile = userPort.getProfile(learnerId)
                .orElseThrow(() -> new IllegalArgumentException("학습자를 찾을 수 없습니다: " + learnerId));

        // 2. 학습 이력
        List<LearningHistoryData> historyData = historyPort.findByLearnerId(learnerId);
        List<LearningHistory> histories = historyData.stream()
                .map(h -> new LearningHistory(
                        CourseId.of(h.courseId()),
                        EnrollmentStatus.valueOf(h.status())
                ))
                .toList();

        // 3. 강좌 후보
        List<CourseMetaData> courseMetas = coursePort.findAll();
        List<CourseCandidate> candidates = courseMetas.stream()
                .map(meta -> new CourseCandidate(
                        CourseId.of(meta.courseId()),
                        new HashSet<>(meta.tags()),
                        Level.fromString(meta.difficulty()),
                        meta.isPublic()
                ))
                .toList();

        // 4. Context 생성
        return RecommendContext.create(
                new HashSet<>(profile.explicitTags()),
                histories,
                candidates
        );
    }

    /**
     * Tier 1: 기존 태그 매칭 추천
     */
    private List<RecommendedCourse> calculateTier1(RecommendContext context) {
        log.debug("[Tier1] Starting calculation");

        // 필터링된 후보만 계산
        List<CourseCandidate> filtered = context.getFilteredCandidates();
        TagContext tagContext = context.getTagContext();

        List<ScoredItem> scored = filtered.stream()
                .map(candidate -> {
                    double score = tier1Policy.calculateScore(
                            candidate.getTags(),
                            tagContext
                    );
                    return new ScoredItem(candidate.getCourseId(), score, "TIER1");
                })
                .filter(item -> item.score > 0)
                .sorted(Comparator.comparingDouble(ScoredItem::score).reversed())
                .limit(tier1Count)
                .toList();

        // RecommendedCourse 변환
        List<RecommendedCourse> result = new ArrayList<>();
        for (int i = 0; i < scored.size(); i++) {
            ScoredItem item = scored.get(i);
            result.add(new RecommendedCourse(item.courseId, item.score, i + 1));
        }

        log.debug("[Tier1] Completed, count={}", result.size());
        return result;
    }

    /**
     * Tier 2: AI 기반 확장 추천
     */
    private List<RecommendedCourse> calculateTier2(RecommendContext context, Long learnerId) {
        try {
            log.debug("[Tier2] Starting AI-based calculation for learnerId={}", learnerId);

            // 1. 페르소나 감지
            TagContext tagContext = context.getTagContext();
            Set<String> allTags = new HashSet<>();
            allTags.addAll(tagContext.explicitTags());
            allTags.addAll(tagContext.implicitTags());

            PersonaType persona = personaDetectionService.detectPersona(allTags);
            log.info("[Tier2] Detected persona={} for learnerId={}", persona, learnerId);

            // 2. 페르소나 컨텍스트 생성
            // Note: Level 정보가 Context에 없으므로 기본값 사용
            PersonaContext personaContext = new PersonaContext(
                    persona,
                    allTags,
                    "MIDDLE"  // 기본 레벨 (추후 Context에서 추출 가능)
            );

            // 3. AI 점수 계산
            List<CourseCandidate> filtered = context.getFilteredCandidates();
            List<ScoredItem> scored = filtered.stream()
                    .map(candidate -> {
                        double score = tier2Strategy.calculateScore(
                                candidate.getTags(),
                                personaContext
                        );
                        return new ScoredItem(candidate.getCourseId(), score, "TIER2");
                    })
                    .filter(item -> item.score > 0)
                    .sorted(Comparator.comparingDouble(ScoredItem::score).reversed())
                    .limit(tier2Count)
                    .toList();

            // RecommendedCourse 변환
            List<RecommendedCourse> result = new ArrayList<>();
            for (int i = 0; i < scored.size(); i++) {
                ScoredItem item = scored.get(i);
                result.add(new RecommendedCourse(item.courseId, item.score, i + 1));
            }

            log.debug("[Tier2] Completed, count={}", result.size());
            return result;

        } catch (Exception e) {
            log.error("[Tier2] Failed for learnerId={}, returning empty", learnerId, e);
            return Collections.emptyList();
        }
    }

    /**
     * Tier 1 + Tier 2 통합 (중복 제거)
     */
    private List<RecommendedCourse> mergeTiers(
            List<RecommendedCourse> tier1,
            List<RecommendedCourse> tier2
    ) {
        Map<CourseId, RecommendedCourse> merged = new LinkedHashMap<>();

        // Tier 1 우선 추가
        for (RecommendedCourse course : tier1) {
            merged.put(course.getCourseId(), course);
        }

        // Tier 2 추가 (중복 시 Tier 1 유지)
        for (RecommendedCourse course : tier2) {
            merged.putIfAbsent(course.getCourseId(), course);
        }

        // 최종 10개 선택 및 순위 재조정
        List<RecommendedCourse> result = new ArrayList<>(merged.values());
        result = result.stream().limit(10).toList();

        // 순위 재조정
        List<RecommendedCourse> reranked = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            RecommendedCourse course = result.get(i);
            reranked.add(new RecommendedCourse(
                    course.getCourseId(),
                    course.getScore(),
                    i + 1  // 새로운 순위
            ));
        }

        return reranked;
    }

    /**
     * 점수 포함 중간 객체
     */
    private record ScoredItem(CourseId courseId, double score, String tier) {}

    private MemberRecommendation findOrCreateRecommendation(MemberId memberId) {
        return recommendationRepository.findByMemberId(memberId)
                .orElseGet(() -> new MemberRecommendation(memberId));
    }
}

/**
 * 2-Tier 추천 프로세스
 *
 * 1. RecommendContext 생성
 *    ↓ explicitTags, learningHistories, courseCandidates
 *
 * 2. Tier 1 계산 (기존 로직)
 *    ↓ ScoringPolicy.calculateScore()
 *    ↓ courseTags vs TagContext 비교
 *    ↓ Explicit 매칭: 1.0점, Implicit 매칭: 1.5점
 *    ↓ 상위 6개 선택
 *
 * 3. Tier 2 계산 (AI 확장) 🆕
 *    ↓ PersonaDetectionService.detectPersona()
 *    ↓ KnowledgeExpansionPort.expandTechStack()
 *    ↓ Gemini API 호출 → 확장 키워드 생성
 *    ↓ LangChainExpansionStrategy.calculateScore()
 *    ↓ 상위 4개 선택
 *
 * 4. 통합 및 중복 제거
 *    ↓ Tier 1 (6개) + Tier 2 (4개)
 *    ↓ 중복 시 Tier 1 우선
 *    ↓ 최종 10개 저장
 */
