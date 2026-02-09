package com.lxpbe.course.dto;

import com.lxpbe.course.domain.Course;
import com.lxpbe.course.port.InstructorInfo;
import com.lxpbe.course.port.TagInfo;

import java.time.Instant;
import java.util.List;

public record CourseListResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelDto level,
        InstructorInfo instructor,
        List<TagInfo> tags,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseListResponse of(Course course, InstructorInfo instructor, List<TagInfo> tags) {
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
