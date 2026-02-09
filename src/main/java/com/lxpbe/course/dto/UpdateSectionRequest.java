package com.lxpbe.course.dto;

import java.util.List;

public record UpdateSectionRequest(
        Long id,
        String title,
        List<UpdateLectureRequest> lectures
) {
}
