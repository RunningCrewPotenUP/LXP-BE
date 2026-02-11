package com.lxpbe.user.infrastructure.facade.dto;

import com.lxpbe.user.domain.enums.Level;
import java.util.List;
import java.util.Objects;

public record UserProfileDto(
       Long userId,
       List<String> interestTags,
       Level level
) {

    public UserProfileDto {
        Objects.requireNonNull(userId, "userId는 null일 수 없습니다.");
        Objects.requireNonNull(interestTags, "interestTags는 null일 수 없습니다.");
        Objects.requireNonNull(level, "level은 null일 수 없습니다.");
    }
}
