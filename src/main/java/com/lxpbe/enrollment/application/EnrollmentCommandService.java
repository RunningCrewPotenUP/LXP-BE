package com.lxpbe.enrollment.application;

import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.presentation.response.EnrollmentCreatedResponse;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentCommandService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentCommandService(EnrollmentRepository enrollmentRepository) {
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
}
