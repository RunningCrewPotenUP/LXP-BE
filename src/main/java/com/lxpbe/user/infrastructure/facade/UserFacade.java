package com.lxpbe.user.infrastructure.facade;

import com.lxpbe.user.application.service.UserQueryService;
import com.lxpbe.user.infrastructure.facade.dto.UserProfileDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserQueryService userQueryService;

    public Optional<UserProfileDto> getProfile(Long userId) {
        return userQueryService.findById(userId);
    }
}
