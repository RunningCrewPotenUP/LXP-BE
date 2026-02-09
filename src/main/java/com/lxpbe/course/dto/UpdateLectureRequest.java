package com.lxpbe.course.dto;

public record UpdateLectureRequest(
        Long id,
        String title,
        String videoUrl
) {
}
