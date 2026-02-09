package com.lxpbe.course.dto;

import com.lxpbe.course.domain.Section;

import java.util.List;

public record SectionDto(
        Long id,
        String title,
        int durationInSeconds,
        int order,
        List<LectureDto> lectures
) {
    public static SectionDto from(Section section) {
        List<LectureDto> lectureDtos = section.getLectures().stream()
                .map(LectureDto::from)
                .toList();

        return new SectionDto(
                section.getId(),
                section.getTitle(),
                section.getTotalDurationSeconds(),
                section.getOrder(),
                lectureDtos
        );
    }
}
