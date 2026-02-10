package com.lxpbe.course.presentation.response;

import com.lxpbe.course.domain.enums.Level;

public record LevelResponse(
        String key,
        String value
) {
    public static LevelResponse from(Level level) {
        if (level == null) {
            return null;
        }
        return new LevelResponse(level.name(), level.description());
    }
}
