package com.lxpbe.course.application.command;

import com.lxpbe.course.domain.enums.Level;
import com.lxpbe.course.presentation.request.UpdateCourseRequest;

import java.util.List;

public record CourseUpdateCommand(
        Long courseId,
        String title,
        String description,
        String thumbnailUrl,
        Level level,
        List<Long> tags,
        List<SectionUpdateCommand> sections
) {
    public static CourseUpdateCommand of(Long courseId, UpdateCourseRequest request) {
        return new CourseUpdateCommand(
                courseId,
                request.title(),
                request.description(),
                request.thumbnailUrl(),
                request.level(),
                request.tags(),
                request.sections() != null ? request.sections().stream().map(SectionUpdateCommand::from).toList() : null
        );
    }
}
