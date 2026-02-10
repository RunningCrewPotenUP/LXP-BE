package com.lxpbe.enrollment.repository;

import com.lxpbe.enrollment.domain.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndCourseIdAndCancelledAtIsNull(Long userId, Long courseId);
}
