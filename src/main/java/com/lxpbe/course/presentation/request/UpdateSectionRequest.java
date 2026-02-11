package com.lxpbe.course.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UpdateSectionRequest(
        @Schema(example = "1")
        Long id,

        @Schema(example = "1장. 시작하기 (수정)")
        String title,

        List<UpdateLectureRequest> lectures
) {
}
