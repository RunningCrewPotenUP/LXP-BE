package com.lxpbe.enrollment.application.facade;

import com.lxpbe.enrollment.application.facade.dto.EnrollmentHistoryDto;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnrollmentFacade {

    EnrollmentRepository enrollmentRepository;

    public List<EnrollmentHistoryDto> findByUserId(Long userId) {
        return enrollmentRepository.findAllByUserId(userId).stream()
                .filter(enrollment -> enrollment.enrollmentStatus() != EnrollmentStatus.CANCELLED)
                .map(EnrollmentHistoryDto::of)
                .toList();
    }
}
