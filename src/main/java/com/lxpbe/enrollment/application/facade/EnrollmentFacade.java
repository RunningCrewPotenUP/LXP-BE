package com.lxpbe.enrollment.application.facade;

import com.lxpbe.enrollment.application.facade.dto.EnrollmentHistoryDto;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EnrollmentFacade {

    private final EnrollmentRepository enrollmentRepository;

    public List<EnrollmentHistoryDto> findByUserId(Long userId) {
        return enrollmentRepository.findAllByUserId(userId).stream()
                .filter(enrollment -> enrollment.enrollmentStatus() != EnrollmentStatus.CANCELLED)
                .map(EnrollmentHistoryDto::of)
                .toList();
    }
}
