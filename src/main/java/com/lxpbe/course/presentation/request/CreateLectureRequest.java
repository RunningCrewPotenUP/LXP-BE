package com.lxpbe.course.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CreateLectureRequest(
        @NotBlank(message = "강의 제목은 필수입니다")
        String title,
        String videoUrl,
        Long durationSeconds
) {
}
