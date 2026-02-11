package com.lxpbe.enrollment.repository.view;

import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import com.lxpbe.tag.domain.Tag;

import java.time.Instant;
import java.util.List;

public interface EnrollmentDetailsView {

    Long getEnrollmentId();
    EnrollmentStatus getEnrollmentStatus();
    Instant getEnrolledAt();
    Instant getLearningStartedAt();
    Instant getCancelledAt();
    CancelType getCancelType();
    CancelReasonType getCancelReasonType();
    String getCancelReasonComment();

    Long getInstructorId();
    String getInstructorName();

    Long getCourseId();
    String getThumbnailUrl();
    String getCourseTitle();
    String getCourseDescription();
    String getCourseLevel();

    List<Tag> getTags();

    // To Do: 진행도 구현 후 반영
    // Double getTotalProgress();
}
