package com.lxpbe.course.presentation.response;

import com.lxpbe.course.domain.Course;
import com.lxpbe.course.application.port.TagResult;

import java.time.Instant;
import java.util.List;

public record CourseListResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelResponse level,
        InstructorResponse instructor,
        List<TagResult> tags,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseListResponse of(Course course, InstructorResponse instructor, List<TagResult> tags) {
        return new CourseListResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                LevelResponse.from(course.getDifficulty()),
                instructor,
                tags,
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
