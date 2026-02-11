package com.lxpbe.course.presentation.request;

import com.lxpbe.course.domain.enums.Level;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UpdateCourseRequest(
        @Schema(example = "Spring Boot 심화")
        String title,

        @Schema(example = "Spring Boot 심화 과정")
        String description,

        @Schema(example = "https://example.com/thumbnail.jpg")
        String thumbnailUrl,

        @Schema(example = "MIDDLE")
        Level level,

        @Schema(example = "[1, 3]")
        List<Long> tags,

        List<UpdateSectionRequest> sections
) {
}
