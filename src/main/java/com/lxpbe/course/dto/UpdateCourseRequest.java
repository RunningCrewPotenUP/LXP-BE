package com.lxpbe.course.dto;

import com.lxpbe.course.domain.enums.Level;

import java.util.List;

public record UpdateCourseRequest(
        String title,
        String description,
        String thumbnailUrl,
        Level level,
        List<Long> tags,
        List<UpdateSectionRequest> sections
) {
}
