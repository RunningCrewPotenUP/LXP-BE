package com.lxpbe.course.dto;

import com.lxpbe.course.domain.Course;
import com.lxpbe.course.port.InstructorInfo;
import com.lxpbe.course.port.TagInfo;

import java.time.Instant;
import java.util.List;

public record CourseDetailResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelDto level,
        InstructorInfo instructor,
        List<TagInfo> tags,
        int durationInHours,
        List<SectionDto> sections,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseDetailResponse of(Course course, InstructorInfo instructor, List<TagInfo> tags) {
        List<SectionDto> sectionDtos = course.getSections().stream()
                .map(SectionDto::from)
                .toList();

        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailUrl(),
                LevelDto.from(course.getDifficulty()),
                instructor,
                tags,
                course.getTotalDurationHours(),
                sectionDtos,
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
