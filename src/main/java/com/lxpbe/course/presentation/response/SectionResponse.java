package com.lxpbe.course.presentation.response;

import com.lxpbe.course.domain.Section;

import java.util.List;

public record SectionResponse(
        Long id,
        String title,
        int durationInSeconds,
        int order,
        List<LectureResponse> lectures
) {
    public static SectionResponse from(Section section) {
        List<LectureResponse> lectureResponses = section.getLectures().stream()
                .map(LectureResponse::from)
                .toList();

        return new SectionResponse(
                section.getId(),
                section.getTitle(),
                section.getTotalDurationSeconds(),
                section.getOrder(),
                lectureResponses
        );
    }
}
