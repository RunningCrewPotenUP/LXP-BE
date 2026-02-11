package com.lxpbe.course.presentation.response;

import com.lxpbe.course.domain.Course;
import com.lxpbe.tag.presentation.response.TagResponse;

import java.time.Instant;
import java.util.List;

public record CourseListResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelResponse level,
        InstructorResponse instructor,
        List<TagResponse> tags,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseListResponse of(Course course, InstructorResponse instructor, List<TagResponse> tags) {
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
