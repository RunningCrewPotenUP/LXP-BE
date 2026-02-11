package com.lxpbe.course.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateSectionRequest(
        @NotBlank(message = "섹션 제목은 필수입니다")
        @Schema(example = "1장. 시작하기")
        String title,

        @NotEmpty(message = "섹션에는 최소 1개의 강의가 필요합니다")
        @Valid
        List<CreateLectureRequest> lectures
) {
}
