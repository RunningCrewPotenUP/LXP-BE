package com.lxpbe.enrollment.application.result;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import com.lxpbe.enrollment.repository.view.EnrollmentDetailsView;
import com.lxpbe.tag.domain.Tag;
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

        List<Tag> tags,                 // 1 개 이상의 태그를 포함하고 있어야 함
        
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

    public static EnrollmentDetails of(EnrollmentDetailsView view) {
        return EnrollmentDetails.builder()
                .id(view.getEnrollmentId())
                .courseId(view.getCourseId())
                .status(view.getEnrollmentStatus())
                .enrolledAt(view.getEnrolledAt())
                .learningStartedAt(view.getLearningStartedAt())

                .cancelledAt(view.getCancelledAt())
                .cancelType(view.getCancelType())
                .reasonType(view.getCancelReasonType())
                .reason(view.getCancelReasonComment())

                .instructorId(view.getInstructorId())
                .instructorName(view.getInstructorName())

                .thumbnailUrl(view.getThumbnailUrl())
                .courseTitle(view.getCourseTitle())
                .courseDescription(view.getCourseDescription())
                .courseLevel(view.getCourseLevel())
                .tags(view.getTags())

                // To Do: Progress 개발 후 여기에 적용
                .totalProgress(0.0)

                .build();
    }
}
