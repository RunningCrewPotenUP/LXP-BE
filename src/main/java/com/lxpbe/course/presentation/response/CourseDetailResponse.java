package com.lxpbe.course.presentation.response;

import com.lxpbe.course.application.dto.LevelDto;
import com.lxpbe.course.application.dto.SectionDto;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.application.port.InstructorResult;
import com.lxpbe.course.application.port.TagResult;

import java.time.Instant;
import java.util.List;

public record CourseDetailResponse(
        Long id,
        String title,
        String description,
        String thumbnailUrl,
        LevelDto level,
        InstructorResult instructor,
        List<TagResult> tags,
        int durationInHours,
        List<SectionDto> sections,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseDetailResponse of(Course course, InstructorResult instructor, List<TagResult> tags) {
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
