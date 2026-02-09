package com.lxpbe.course.dto;

import com.lxpbe.course.domain.enums.Level;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCourseRequest(
        @NotBlank(message = "강좌 제목은 필수입니다")
        String title,
        String description,
        String thumbnailUrl,
        @NotNull(message = "난이도는 필수입니다")
        Level level,
        List<Long> tags,
        @Valid
        List<CreateSectionRequest> sections
) {
}
