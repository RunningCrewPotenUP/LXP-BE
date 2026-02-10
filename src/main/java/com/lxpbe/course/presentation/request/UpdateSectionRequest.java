package com.lxpbe.course.presentation.request;

import java.util.List;

public record UpdateSectionRequest(
        Long id,
        String title,
        List<UpdateLectureRequest> lectures
) {
}
