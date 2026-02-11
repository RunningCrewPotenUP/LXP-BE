package com.recommend.application.service;

import com.recommend.application.dto.CourseMetaData;
import com.recommend.application.port.provided.persistence.MemberRecommendationRepository;
import com.recommend.application.port.required.CourseMetaQueryPort;
import com.recommend.domain.model.MemberRecommendation;
import com.recommend.domain.model.RecommendedCourse;
import com.recommend.domain.model.ids.MemberId;
import com.recommend.infrastructure.web.dto.response.RecommendedCourseResponse;
import com.recommend.infrastructure.web.dto.response.RecommendedCourseResponse.CourseInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendQueryService {

    private static final int DEFAULT_TOP_N = 10;
    private final MemberRecommendationRepository recommendationRepository;
    private final CourseMetaQueryPort courseMetaQueryPort;

    /**
     * 기본 상위 10개 추천 조회
     */
    @Transactional(readOnly = true)
    public List<RecommendedCourseResponse> getTopRecommendations(Long memberId) {
        return getTopRecommendations(memberId, DEFAULT_TOP_N);
    }

    /**
     * 추천 결과 조회 (개수 지정)
     */
    @Transactional(readOnly = true)
    public List<RecommendedCourseResponse> getTopRecommendations(Long memberId, int topN) {
        log.info("[추천 조회] memberId={}, topN={}", memberId, topN);

        MemberId memberIdObj = MemberId.of(memberId);

        // 1. Repository에서 조회
        MemberRecommendation recommendation = recommendationRepository
                .findByMemberId(memberIdObj)
                .orElse(null);

        // 2. 추천이 없으면 빈 리스트 반환
        if (recommendation == null || recommendation.isEmpty()) {
            log.info("[추천 없음] memberId={}", memberId);
            return Collections.emptyList();
        }

        // 3. 추천 목록 추출
        List<RecommendedCourse> items = recommendation.getItems().stream()
                .limit(topN)
                .toList();

        // 4. Course ID 목록 추출
        List<Long> courseIds = items.stream()
                .map(item -> item.getCourseId().getValue())
                .toList();

        // 5. Course 메타 정보 조회 (Application DTO)
        List<CourseMetaData> courseMetas = courseMetaQueryPort.findByCourses(courseIds);  // ✅ 기존 메서드명 유지

        // 6. Course ID → CourseMetaData 매핑
        Map<Long, CourseMetaData> courseMetaMap = courseMetas.stream()
                .collect(Collectors.toMap(CourseMetaData::courseId, Function.identity()));

        // 7. 결과 조합 (Application DTO → Web DTO 변환)
        return items.stream()
                .map(item -> {
                    CourseMetaData courseMeta = courseMetaMap.get(item.getCourseId().getValue());
                    if (courseMeta == null) {
                        log.warn("[강좌 메타 없음] courseId={}", item.getCourseId().getValue());
                        return null;
                    }

                    // ✅ CourseMetaData → CourseInfo 변환
                    CourseInfo courseInfo = new CourseInfo(
                            courseMeta.courseId(),
                            "강좌 제목",  // ← Adapter에서 title 포함 필요 (다음 단계)
                            courseMeta.tags(),
                            courseMeta.difficulty(),
                            courseMeta.isPublic()
                    );

                    return new RecommendedCourseResponse(
                            courseInfo,
                            item.getScore(),
                            item.getRank()
                    );
                })
                .filter(response -> response != null)
                .toList();
    }
}
