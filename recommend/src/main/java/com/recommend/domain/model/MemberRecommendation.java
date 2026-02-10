package com.recommend.domain.model;

import com.recommend.domain.exception.DuplicateCourseException;
import com.recommend.domain.exception.RecommendLimitExceededException;
import com.recommend.domain.model.ids.CourseId;
import com.recommend.domain.model.ids.MemberId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 회원별 추천 강좌 Aggregate Root
 *
 * 책임:
 * 1. 추천 아이템의 생명주기 관리
 * 2. 불변식 보장
 * 3. 도메인 이벤트 관리 (내장)
 */
public class MemberRecommendation {

    private static final int MAX_RECOMMENDATION_SIZE = 10;

    // ===== Aggregate Root 필드 (이벤트 관리) =====
    private final transient List<Object> domainEvents = new ArrayList<>();

    // ===== Entity 필드 =====
    private Long id; // DB PK (nullable)
    private MemberId memberId;
    private List<RecommendedCourse> items;
    private LocalDateTime calculatedAt;

    // ===== 생성자 =====

    protected MemberRecommendation() {} // JPA 및 재구성용

    public MemberRecommendation(MemberId memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId는 null일 수 없습니다.");
        }
        this.memberId = memberId;
        this.items = new ArrayList<>();
        this.calculatedAt = LocalDateTime.now();
    }

    // ===== 재구성 메서드 (JPA Entity → Domain 변환 시 사용) =====

    public static MemberRecommendation reconstruct(
            Long id,
            MemberId memberId,
            List<RecommendedCourse> items,
            LocalDateTime calculatedAt
    ) {
        MemberRecommendation recommendation = new MemberRecommendation();
        recommendation.id = id;
        recommendation.memberId = memberId;
        recommendation.items = new ArrayList<>(items);
        recommendation.calculatedAt = calculatedAt;
        return recommendation;
    }

    // ===== 비즈니스 로직: 추천 아이템 업데이트 =====

    /**
     * 추천 아이템을 업데이트하고 불변식을 검증
     *
     * @param newItems 새로운 추천 아이템 리스트
     * @throws RecommendLimitExceededException 10개 초과 시
     * @throws DuplicateCourseException 중복 강좌 포함 시
     */
    public void updateItems(List<RecommendedCourse> newItems) {
        validateItems(newItems);
        this.items = new ArrayList<>(newItems);
        this.calculatedAt = LocalDateTime.now();
    }

    // ===== 불변식 검증 =====

    private void validateItems(List<RecommendedCourse> items) {
        if (items == null) {
            throw new IllegalArgumentException("items는 null일 수 없습니다.");
        }

        // 불변식 1: 최대 10개
        if (items.size() > MAX_RECOMMENDATION_SIZE) {
            throw new RecommendLimitExceededException(
                    String.format("추천 아이템은 최대 %d개까지 가능합니다. (현재: %d개)",
                            MAX_RECOMMENDATION_SIZE, items.size())
            );
        }

        // 불변식 2: 중복 강좌 없음
        Set<CourseId> courseIds = new HashSet<>();
        for (RecommendedCourse item : items) {
            if (!courseIds.add(item.getCourseId())) {
                throw new DuplicateCourseException(
                        "중복된 강좌가 포함되어 있습니다: " + item.getCourseId().getValue()
                );
            }
        }

        // 불변식 3: 순위 연속성 검증 (1, 2, 3, ...)
        for (int i = 0; i < items.size(); i++) {
            int expectedRank = i + 1;
            int actualRank = items.get(i).getRank();
            if (actualRank != expectedRank) {
                throw new IllegalArgumentException(
                        String.format("순위가 연속적이지 않습니다. 기대: %d, 실제: %d", expectedRank, actualRank)
                );
            }
        }
    }

    // ===== 도메인 이벤트 관리 (Aggregate Root 역할) =====

    /**
     * 도메인 이벤트 등록
     */
    protected void registerEvent(Object event) {
        if (event == null) {
            throw new IllegalArgumentException("Domain event must not be null");
        }
        this.domainEvents.add(event);
    }

    /**
     * 등록된 도메인 이벤트 조회 (불변 리스트)
     */
    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 도메인 이벤트 초기화 (이벤트 발행 후 호출)
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public List<RecommendedCourse> getItems() {
        return List.copyOf(items); // 불변 리스트 반환
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // ===== Inner Class: ScoredCourse (점수 포함 VO) =====

    /**
     * 점수 계산 후 사용되는 임시 VO
     * (도메인 서비스 → Aggregate 전달 시 사용)
     */
    public record ScoredCourse(
            CourseId courseId,
            double score
    ) {
        public ScoredCourse {
            if (courseId == null) {
                throw new IllegalArgumentException("courseId는 null일 수 없습니다.");
            }
            if (score < 0) {
                throw new IllegalArgumentException("점수는 0 이상이어야 합니다.");
            }
        }
    }
}
