package com.lxpbe.course.dto;

import com.lxpbe.course.domain.enums.Level;

public record LevelDto(
        String key,
        String value
) {
    public static LevelDto from(Level level) {
        if (level == null) {
            return null;
        }
        return new LevelDto(level.name(), level.description());
    }
}
