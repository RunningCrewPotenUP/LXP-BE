package com.lxpbe.course.presentation.response;

import com.lxpbe.course.domain.Course;
import com.lxpbe.tag.application.result.TagResult;

import java.time.Instant;
import java.util.List;

public record CourseDetailResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelResponse level,
        InstructorResponse instructor,
        List<TagResponse> tags,
        int durationInHours,
        List<SectionResponse> sections,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseDetailResponse of(Course course, InstructorResponse instructor, List<TagResult> tags) {
        List<SectionResponse> sections = course.getSections().stream()
                .map(SectionResponse::from)
                .toList();

        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                LevelResponse.from(course.getDifficulty()),
                instructor,
                tags.stream().map(TagResponse::from).toList(),
                course.getTotalDurationHours(),
                sections,
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
