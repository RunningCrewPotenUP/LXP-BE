package com.lxpbe.course.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateLectureRequest(
        @Schema(example = "1")
        Long id,

        @Schema(example = "강의 소개 (수정)")
        String title,

        @Schema(example = "https://example.com/video-v2.mp4")
        String videoUrl,

        @Schema(example = "2400")
        Long durationSeconds
) {
}
