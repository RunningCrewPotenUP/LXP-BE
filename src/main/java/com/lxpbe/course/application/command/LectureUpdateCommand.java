package com.lxpbe.course.application.command;

import com.lxpbe.course.presentation.request.UpdateLectureRequest;

public record LectureUpdateCommand(
        Long id,
        String title,
        String videoUrl,
        Long durationSeconds
) {
    public static LectureUpdateCommand from(UpdateLectureRequest request) {
        return new LectureUpdateCommand(
                request.id(),
                request.title(),
                request.videoUrl(),
                request.durationSeconds()
        );
    }
}
