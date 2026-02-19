package com.recommend.application.dto;

/**
 * 학습 이력 데이터
 */
public record LearningHistoryData(
        Long courseId,    // 
        String status     // "ENROLLED", "COMPLETED", "CANCELED"
) {}
