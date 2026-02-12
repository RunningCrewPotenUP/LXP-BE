package com.lxpbe.enrollment.repository.projection;

import com.lxpbe.course.domain.enums.Level;
import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;

import java.time.Instant;

public interface EnrollmentSummaryProjectionRow {
    Long getId();
    Long getCourseId();
    EnrollmentStatus getStatus();
    Instant getEnrolledAt();
    Instant getLearningStartedAt();
    Instant getCancelledAt();
    CancelType getCancelType();
    CancelReasonType getReasonType();
    String getReason();
    Long getInstructorId();
    String getInstructorName();
    String getThumbnailUrl();
    String getCourseTitle();
    String getCourseDescription();
    Level getCourseLevel();
}
