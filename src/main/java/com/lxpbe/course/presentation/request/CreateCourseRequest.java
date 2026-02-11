package com.lxpbe.course.presentation.request;

import com.lxpbe.course.domain.enums.Level;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCourseRequest(
        @NotBlank(message = "강좌 제목은 필수입니다")
        @Schema(example = "Spring Boot 입문")
        String title,

        @Schema(example = "Spring Boot 기초부터 실전까지")
        String description,

        @Schema(example = "https://example.com/thumbnail.jpg")
        String thumbnailUrl,

        @NotNull(message = "난이도는 필수입니다")
        @Schema(example = "JUNIOR")
        Level level,

        @Schema(example = "[1, 2]")
        List<Long> tags,

        @Valid
        List<CreateSectionRequest> sections
) {
}
