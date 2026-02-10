package com.recommend.infrastructure.adapter;

import com.recommend.application.dto.LearnerProfileData;
import com.recommend.application.port.required.LearnerProfileQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Member BC 연동 Adapter
 * - HTTP 호출 제거, 로컬 Service 직접 호출
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LearnerProfileAdapter implements LearnerProfileQueryPort {

    // TODO: Member BC의 실제 Service 주입
    // private final MemberQueryService memberQueryService;

    @Override
    public Optional<LearnerProfileData> getProfile(Long learnerId) {
        log.debug("[LearnerProfileAdapter] 학습자 프로필 조회: learnerId={}", learnerId);

        // TODO: Member BC Service 호출로 교체
        // return memberQueryService.findById(learnerId)
        //     .map(this::toLearnerProfileData);

        throw new UnsupportedOperationException("Member BC Service 연동 필요");
    }

    /**
     * Member Domain Model → LearnerProfileData DTO 변환
     * (Anti-Corruption Layer)
     */
    private LearnerProfileData toLearnerProfileData(Object member) {
        // TODO: Member BC의 실제 Domain Model로 변환
        // return new LearnerProfileData(
        //     member.getId(),              // Long
        //     member.getLevel(),           // String
        //     member.getExplicitTags(),    // Set<String>
        //     member.getImplicitTags()     // Set<String>
        // );

        throw new UnsupportedOperationException("Member BC Domain Model 매핑 필요");
    }
}
