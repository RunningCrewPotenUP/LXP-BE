package com.lxpbe.course.application.command;

import com.lxpbe.course.presentation.request.UpdateSectionRequest;

import java.util.List;

public record SectionUpdateCommand(
        Long id,
        String title,
        List<LectureUpdateCommand> lectures
) {
    public static SectionUpdateCommand from(UpdateSectionRequest request) {
        return new SectionUpdateCommand(
                request.id(),
                request.title(),
                request.lectures() != null ? request.lectures().stream().map(LectureUpdateCommand::from).toList() : null
        );
    }
}
