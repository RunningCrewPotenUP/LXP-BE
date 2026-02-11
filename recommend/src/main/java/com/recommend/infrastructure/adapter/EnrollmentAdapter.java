package com.recommend.infrastructure.adapter;

import com.lxpbe.enrollment.infrastructure.facade.EnrollmentFacade;
import com.lxpbe.enrollment.infrastructure.facade.dto.EnrollmentHistoryDto;
import com.recommend.application.dto.LearningHistoryData;
import com.recommend.application.port.required.LearningHistoryQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Enrollment BC Adapter
 * - LearningHistoryQueryPort 구현
 * - EnrollmentFacade를 통해 수강 이력 조회
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentAdapter implements LearningHistoryQueryPort {

    private final EnrollmentFacade enrollmentFacade;  // ✅ Enrollment BC Facade 주입

    /**
     * 학습자의 수강 이력 조회
     *
     * @param userId 학습자 ID
     * @return 수강 이력 데이터 리스트
     */
    @Override
    public List<LearningHistoryData> findByUserId(Long userId) {
        log.debug("[EnrollmentAdapter] Fetching learning history for userId={}", userId);

        try {
            // 1. EnrollmentFacade 호출
            List<EnrollmentHistoryDto> dtos = enrollmentFacade.findByUserId(userId);

            // 2. Facade DTO → Application DTO 변환
            return dtos.stream()
                    .map(dto -> new LearningHistoryData(
                            dto.courseId(),  // courseId만 추출
                            dto.status()     // status 그대로 전달
                    ))
                    .toList();

        } catch (Exception e) {
            log.error("[EnrollmentAdapter] Failed to fetch learning history for userId={}", userId, e);
            return List.of();  // ✅ 예외 발생 시 빈 리스트 반환
        }
    }
}
