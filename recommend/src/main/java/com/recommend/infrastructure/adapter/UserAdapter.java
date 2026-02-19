package com.recommend.infrastructure.adapter;

import com.lxpbe.user.infrastructure.facade.UserFacade;
import com.lxpbe.user.infrastructure.facade.dto.UserProfileDto;
import com.recommend.application.dto.LearnerProfileData;
import com.recommend.application.port.required.LearnerProfileQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAdapter implements LearnerProfileQueryPort {

    private final UserFacade userFacade;

    @Override
    public Optional<LearnerProfileData> getProfile(Long userId) {
        log.debug("[UserAdapter] Fetching profile for userId={}", userId);

        try {
            Optional<UserProfileDto> profileDto = userFacade.getProfile(userId);

            return profileDto.map(dto -> new LearnerProfileData(
                    dto.userId(),                           // Long
                    new HashSet<>(dto.interestTags()),      // Set<String> (explicitTags)
                    Set.of(),                               // Set<String> (implicitTags - 빈 Set)
                    dto.level().name()                      // ✅ Enum → String 변환
            ));

        } catch (Exception e) {
            log.error("[UserAdapter] Failed to fetch profile for userId={}", userId, e);
            return Optional.empty();
        }
    }
}
