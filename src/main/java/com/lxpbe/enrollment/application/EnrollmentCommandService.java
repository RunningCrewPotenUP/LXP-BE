package com.lxpbe.enrollment.application;

import com.lxpbe.enrollment.application.command.EnrollmentCancelCommand;
import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.presentation.response.EnrollmentCancelledResponse;
import com.lxpbe.enrollment.presentation.response.EnrollmentCreatedResponse;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentCommandService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentCommandService(
            EnrollmentRepository enrollmentRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentCreatedResponse enroll(Long userId, Long courseId) {

        Optional<Enrollment> optionalEnrollment
                = enrollmentRepository.findByUserIdAndCourseIdAndCancelledAtIsNull(userId, courseId);

        if (optionalEnrollment.isPresent()) {
            throw new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_ALREADY_EXISTS);
        }

        Enrollment saved = enrollmentRepository.save(
                Enrollment.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .build()
        );

        return EnrollmentCreatedResponse.builder()
                .id(saved.id())
                .courseId(saved.courseId())
                .status(saved.enrollmentStatus())
                .enrolledAt(saved.enrolledAt())
                .build();
    }

    public EnrollmentCancelledResponse cancelByUser(EnrollmentCancelCommand command) {

        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(command.enrollmentId());
        if (optionalEnrollment.isEmpty()) {
            throw new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_EXISTS);
        }

        Enrollment target = optionalEnrollment.get();
        target.cancel(CancelType.SELF_SERVICE, command.reasonType(), command.reason());
        enrollmentRepository.save(target);

        return EnrollmentCancelledResponse.builder()
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
