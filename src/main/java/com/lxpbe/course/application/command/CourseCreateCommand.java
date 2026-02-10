package com.lxpbe.course.application.command;

import com.lxpbe.course.domain.enums.Level;
import com.lxpbe.course.presentation.request.CreateCourseRequest;

import java.util.Collections;
import java.util.List;

public record CourseCreateCommand(
       String title,
       String description,
       String thumbnailUrl,
       Level level,
       List<Long> tags,
       List<SectionCreateCommand> sections
) {
    public static CourseCreateCommand from(CreateCourseRequest request) {
        return new CourseCreateCommand(
                request.title(),
                request.description(),
                request.thumbnailUrl(),
                request.level(),
                request.tags() != null ? request.tags() : Collections.emptyList(),
                request.sections() != null ? request.sections().stream().map(SectionCreateCommand::from).toList() : null
        );
    }
}
