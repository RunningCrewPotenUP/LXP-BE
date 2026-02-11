package com.lxpbe.course.infrastructure.facade.dto;

import com.lxpbe.course.application.result.CourseResult;
import com.lxpbe.tag.application.result.TagResult;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record CourseMetadataDto(
        Long courseId,
        String title,
        Set<String> tags,
        String difficulty,
        boolean isPublic
) {
    public CourseMetadataDto {
        Objects.requireNonNull(courseId, "courseId는 null일 수 없습니다.");
        Objects.requireNonNull(title, "title은 null이거나 빈 문자열일 수 없습니다.");
        Objects.requireNonNull(tags, "tags는 null 일 수 없습니다");
        Objects.requireNonNull(difficulty, "difficulty는 null이거나 빈 문자열일 수 없습니다");
    }

    public static CourseMetadataDto from(CourseResult courseResult, List<TagResult> tagResults) {
        return new CourseMetadataDto(
                courseResult.courseId(),
                courseResult.title(),
                tagResults.stream().map(TagResult::name).collect(Collectors.toSet()),
                courseResult.difficulty().name(),
                true
        );
    }
}
