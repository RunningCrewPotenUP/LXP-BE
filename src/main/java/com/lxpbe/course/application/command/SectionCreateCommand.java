package com.lxpbe.course.application.command;

import com.lxpbe.course.presentation.request.CreateSectionRequest;

import java.util.List;

public record SectionCreateCommand(
        String title,
        List<LectureCreateCommand> lectures
) {
    public static SectionCreateCommand from(CreateSectionRequest request) {
        return new SectionCreateCommand(
                request.title(),
                request.lectures() != null ? request.lectures().stream().map(LectureCreateCommand::from).toList() : null
        );
    }
}
