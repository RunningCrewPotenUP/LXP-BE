package com.recommend.application.dto.response;

import com.recommend.application.dto.RecommendedCourseDto;

import java.util.List;

public record RecommendationResponse(
        String memberId,
        List<RecommendedCourseDto> recommendations,
        String calculatedAt
) {}