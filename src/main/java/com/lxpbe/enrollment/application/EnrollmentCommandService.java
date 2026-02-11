package com.lxpbe.enrollment.application;

import com.lxpbe.enrollment.application.command.EnrollmentCancelCommand;
import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.application.result.EnrollmentCancelledResult;
import com.lxpbe.enrollment.application.result.EnrollmentCreatedResult;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EnrollmentCommandService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentCommandService(
            EnrollmentRepository enrollmentRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentCreatedResult enroll(Long requesterId, Long courseId) {

        enrollmentRepository.findByUserIdAndCourseIdAndCancelledAtIsNull(requesterId, courseId)
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_ALREADY_EXISTS));

        Enrollment saved = enrollmentRepository.save(
                Enrollment.builder()
                        .userId(requesterId)
                        .courseId(courseId)
                        .build()
        );

        return EnrollmentCreatedResult.builder()
                .id(saved.id())
                .courseId(saved.courseId())
                .status(saved.enrollmentStatus())
                .enrolledAt(saved.enrolledAt())
                .build();
    }

    public EnrollmentCancelledResult cancelByUser(EnrollmentCancelCommand command) {

        Enrollment target = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND));

        if (!Objects.equals(target.userId(), command.requesterId())) {
            throw new EnrollmentException(EnrollmentErrorCode.FORBIDDEN_ENROLLMENT_CANCEL);
        }

        target.cancel(CancelType.SELF_SERVICE, command.reasonType(), command.reason());
        enrollmentRepository.save(target);

        return EnrollmentCancelledResult.builder()
                .id(target.id())
                .courseId(target.courseId())
                .status(target.enrollmentStatus())
                .enrolledAt(target.enrolledAt())
                .learningStartedAt(target.learningStartedAt())
                .cancelledAt(target.cancelledAt())
                .cancelType(target.cancelType())
                .reasonType(target.cancelReasonType())
                .reason(target.cancelReasonComment())
                .build();
    }
}
