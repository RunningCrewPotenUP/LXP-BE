package com.recommend.infrastructure.adapter;

import com.recommend.application.dto.LearningHistoryData;
import com.recommend.application.port.required.LearningHistoryQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
//이제 사용 안함! 대체
/**
 * Enrollment BC 연동 Adapter
 * - HTTP 호출 제거, 로컬 Service 직접 호출
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LearningHistoryAdapter implements LearningHistoryQueryPort {

    // TODO: Enrollment BC의 실제 Service 주입
    // private final EnrollmentQueryService enrollmentQueryService;

    @Override
    public List<LearningHistoryData> findByLearnerId(Long learnerId) {
        log.debug("[LearningHistoryAdapter] 학습 이력 조회: learnerId={}", learnerId);

        // TODO: Enrollment BC Service 호출로 교체
        // List<Enrollment> enrollments = enrollmentQueryService.findByLearnerId(learnerId);
        // return enrollments.stream()
        //     .map(this::toLearningHistoryData)
        //     .toList();

        throw new UnsupportedOperationException("Enrollment BC Service 연동 필요");
    }

    /**
     * Enrollment Domain Model → LearningHistoryData DTO 변환
     * (Anti-Corruption Layer)
     */
    private LearningHistoryData toLearningHistoryData(Object enrollment) {
        // TODO: Enrollment BC의 실제 Domain Model로 변환
        // return new LearningHistoryData(
        //     enrollment.getCourseId(),    // Long
        //     enrollment.getStatus()       // String (ENROLLED, COMPLETED, CANCELLED)
        // );

        throw new UnsupportedOperationException("Enrollment BC Domain Model 매핑 필요");
    }
}
