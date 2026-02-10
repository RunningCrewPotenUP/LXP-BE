package com.lxpbe.course.presentation.response;

import com.lxpbe.course.domain.Lecture;

public record LectureResponse(
        Long id,
        String title,
        String videoUrl,
        int order,
        Long durationInSeconds
) {
    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getVideoUrl(),
                lecture.getOrder(),
                lecture.getDurationSeconds()
        );
    }
}
