package com.lxpbe.enrollment.application;

import com.lxpbe.enrollment.application.result.EnrollmentDetails;
import com.lxpbe.enrollment.application.result.EnrollmentSummary;
import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EnrollmentQueryService {

    EnrollmentRepository enrollmentRepository;

    public EnrollmentQueryService(
            EnrollmentRepository enrollmentRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentDetails queryDetails(Long requesterId, Long enrollmentId) {

        Enrollment foundEnrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND));

        if (!foundEnrollment.userId().equals(requesterId)
                && !foundEnrollment.courseId().equals(requesterId)
        ) {
            throw new EnrollmentException(EnrollmentErrorCode.FORBIDDEN_ENROLLMENT_QUERY);
        }

        return EnrollmentDetails.of(
                enrollmentRepository.projectEnrollmentDetails(enrollmentId)
        );
    }

    public List<EnrollmentSummary> myEnrollments(Long requesterId) {
        return enrollmentRepository.projectEnrollmentSummaries(requesterId).stream()
                .map(EnrollmentSummary::of)
                .toList();
    }
}
