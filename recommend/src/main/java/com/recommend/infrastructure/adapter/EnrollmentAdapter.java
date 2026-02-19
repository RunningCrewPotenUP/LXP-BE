package com.recommend.infrastructure.adapter;

import com.lxpbe.enrollment.application.facade.EnrollmentFacade;
import com.lxpbe.enrollment.application.facade.dto.EnrollmentHistoryDto;
import com.recommend.application.dto.LearningHistoryData;
import com.recommend.application.port.required.LearningHistoryQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentAdapter implements LearningHistoryQueryPort {

    private final EnrollmentFacade enrollmentFacade;

    @Override
    public List<LearningHistoryData> findByLearnerId(Long learnerId) {  // ✅ 메서드명 수정
        log.debug("[EnrollmentAdapter] Fetching learning history for learnerId={}", learnerId);

        try {
            // Facade는 여전히 findByUserId 사용 (User BC 용어 사용)
            List<EnrollmentHistoryDto> dtos = enrollmentFacade.findByUserId(learnerId);

            return dtos.stream()
                    .map(dto -> new LearningHistoryData(
                            dto.courseId(),
                            dto.status()
                    ))
                    .toList();

        } catch (Exception e) {
            log.error("[EnrollmentAdapter] Failed to fetch learning history for learnerId={}", learnerId, e);
            return List.of();
        }
    }
}
