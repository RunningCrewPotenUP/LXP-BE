package com.lxpbe.course.presentation.response;

import com.lxpbe.course.application.dto.LevelDto;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.application.port.InstructorResult;
import com.lxpbe.course.application.port.TagResult;

import java.time.Instant;
import java.util.List;

public record CourseListResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelDto level,
        InstructorResult instructor,
        List<TagResult> tags,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseListResponse of(Course course, InstructorResult instructor, List<TagResult> tags) {
        return new CourseListResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                LevelDto.from(course.getDifficulty()),
                instructor,
                tags,
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
