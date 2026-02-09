package com.lxpbe.course.application.dto;

import com.lxpbe.course.domain.Lecture;

public record LectureDto(
        Long id,
        String title,
        String videoUrl,
        int order,
        Long durationInSeconds
) {
    public static LectureDto from(Lecture lecture) {
        return new LectureDto(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getVideoUrl(),
                lecture.getOrder(),
                lecture.getDurationSeconds()
        );
    }
}
