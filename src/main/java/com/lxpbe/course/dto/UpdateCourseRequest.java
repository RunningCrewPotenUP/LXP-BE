package com.lxpbe.course.dto;

import java.util.List;

public record UpdateCourseRequest(
        String title,
        String description,
        String thumbnailUrl,
        String level,
        List<Long> tags,
        List<UpdateSectionRequest> sections
) {
}
