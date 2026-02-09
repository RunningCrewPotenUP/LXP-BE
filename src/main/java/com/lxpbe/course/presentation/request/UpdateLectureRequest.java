package com.lxpbe.course.presentation.request;

public record UpdateLectureRequest(
        Long id,
        String title,
        String videoUrl
) {
}
