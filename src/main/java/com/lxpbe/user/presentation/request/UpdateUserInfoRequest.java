package com.lxpbe.user.presentation.request;

import com.lxpbe.user.domain.enums.Level;
import java.util.List;

public record UpdateUserInfoRequest(
        String name,
        List<Long> tagIds,
        Level level
) {
}
