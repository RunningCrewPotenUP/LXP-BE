package com.lxpbe.enrollment.application.result;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import com.lxpbe.enrollment.repository.projection.EnrollmentDetailsProjectionRow;
import com.lxpbe.tag.application.result.TagResult;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Builder
public record EnrollmentDetails(
        Long id,
        Long courseId,
        EnrollmentStatus status,
        Instant enrolledAt,
        Instant learningStartedAt,      // optional
        Instant cancelledAt,            // optional
        CancelType cancelType,          // optional
        CancelReasonType reasonType,    // optional
        String reason,                  // optional

        Long instructorId,
        String instructorName,

        String thumbnailUrl,            // optional
        String courseTitle,
        String courseDescription,
        String courseLevel,

        List<TagResult> tags,                 // 1 개 이상의 태그를 포함하고 있어야 함

        // To Do: 진행도 개발 이후 붙이기
        Double totalProgress
) {

    public EnrollmentDetails {
        final String REQUIRED_FIELD_ERROR_MESSAGE_PREFIX = "수강 상세 조회 시 필수 필드 누락: ";
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "id");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "courseId");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "status");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "enrolledAt");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "instructorId");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "instructorName");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "courseTitle");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "courseDescription");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "courseLevel");
        Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "tags");
        if (tags.isEmpty()) {
            Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "tags");
        }
        // To Do: Progress 개발 후 주석 해제 할 것
        // Objects.requireNonNull(status, REQUIRED_FIELD_ERROR_MESSAGE_PREFIX + "totalProgress");
    }

    public static EnrollmentDetails of(EnrollmentDetailsProjectionRow row, List<TagResult> tags) {
        return EnrollmentDetails.builder()
                .id(row.getId())
                .courseId(row.getCourseId())
                .status(row.getStatus())
                .enrolledAt(row.getEnrolledAt())
                .learningStartedAt(row.getLearningStartedAt())

                .cancelledAt(row.getCancelledAt())
                .cancelType(row.getCancelType())
                .reasonType(row.getReasonType())
                .reason(row.getReason())

                .instructorId(row.getInstructorId())
                .instructorName(row.getInstructorName())

                .thumbnailUrl(row.getThumbnailUrl())
                .courseTitle(row.getCourseTitle())
                .courseDescription(row.getCourseDescription())
                .courseLevel(row.getCourseLevel().name())
                .tags(tags)
                
                // To Do: 진행도(Progress) 구현 후 반영할 것
                .totalProgress(0.0)
                .build();
    }
}
