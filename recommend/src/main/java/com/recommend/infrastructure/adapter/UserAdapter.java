package com.recommend.infrastructure.adapter;

import com.lxpbe.user.infrastructure.facade.UserFacade;
import com.lxpbe.user.infrastructure.facade.dto.UserProfileDto;
import com.recommend.application.dto.LearnerProfileData;
import com.recommend.application.port.required.LearnerProfileQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * User BC Adapter
 * - LearnerProfileQueryPort 구현
 * - UserFacade를 통해 학습자 프로필 조회
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAdapter implements LearnerProfileQueryPort {

    private final UserFacade userFacade;  // ✅ User BC Facade 주입

    /**
     * 학습자 프로필 조회
     *
     * @param userId 학습자 ID
     * @return 학습자 프로필 데이터
     */
    @Override
    public Optional<LearnerProfileData> getProfile(Long userId) {
        log.debug("[UserAdapter] Fetching profile for userId={}", userId);

        try {
            // 1. UserFacade 호출
            Optional<UserProfileDto> profileDto = userFacade.getProfile(userId);

            // 2. Facade DTO → Application DTO 변환
            return profileDto.map(dto -> new LearnerProfileData(
                    dto.userId(),
                    dto.interestTags(),  // Set<String> 그대로 전달
                    dto.level()          // String 그대로 전달
            ));

        } catch (Exception e) {
            log.error("[UserAdapter] Failed to fetch profile for userId={}", userId, e);
            return Optional.empty();  // ✅ 예외 발생 시 빈 Optional 반환
        }
    }
}
