package com.lxpbe.course.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateLectureRequest(
        @NotBlank(message = "강의 제목은 필수입니다")
        @Schema(example = "강의 소개")
        String title,

        @Schema(example = "https://example.com/video.mp4")
        String videoUrl,

        @Schema(example = "1800")
        Long durationSeconds
) {
}
