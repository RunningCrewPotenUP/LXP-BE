package com.lxpbe.course.application.command;

import com.lxpbe.course.presentation.request.CreateLectureRequest;

public record LectureCreateCommand(
        String title,
        String videoUrl,
        Long durationSeconds
) {
    public static LectureCreateCommand from(CreateLectureRequest request) {
        return new LectureCreateCommand(
                request.title(),
                request.videoUrl(),
                request.durationSeconds()
        );
    }
}
