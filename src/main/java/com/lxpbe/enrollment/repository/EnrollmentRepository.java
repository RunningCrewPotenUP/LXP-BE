package com.lxpbe.enrollment.repository;

import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.repository.view.EnrollmentDetailsView;
import com.lxpbe.enrollment.repository.view.EnrollmentSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndCourseIdAndCancelledAtIsNull(Long userId, Long courseId);

    @Query("""
    SELECT e.id as enrollmentId,
            e.enrollmentStatus as enrollmentStatus,
            e.enrolledAt as enrolledAt,
            e.learningStartedAt as learningStartedAt,
            e.cancelledAt as cancelledAt,
            e.cancelType as cancelType,
            e.cancelReasonType as cancelReasonType,
            e.cancelReasonComment as cancelReasonComment,
            u.id as instructorId,
            u.name as instructorName,
            c.id as courseId,
            c.thumbnailUrl as thumbnailUrl,
            c.title as courseTitle,
            c.description as courseDescription,
            c.difficulty as courseLevel,
            c.tags as tags
    FROM Enrollment e
    JOIN Course c ON c.id = e.courseId
    JOIN User u ON u.id = c.instructorId
    WHERE e.id = :enrollmentId
    """)
    EnrollmentDetailsView projectEnrollmentDetails(@Param("enrollmentId") Long enrollmentId);

    @Query("""
    SELECT e.id as enrollmentId,
            e.enrollmentStatus as enrollmentStatus,
            e.enrolledAt as enrolledAt,
            e.learningStartedAt as learningStartedAt,
            e.cancelledAt as cancelledAt,
            e.cancelType as cancelType,
            e.cancelReasonType as cancelReasonType,
            e.cancelReasonComment as cancelReasonComment,
            u.id as instructorId,
            u.name as instructorName,
            c.id as courseId,
            c.thumbnailUrl as thumbnailUrl,
            c.title as courseTitle,
            c.description as courseDescription,
            c.difficulty as courseLevel,
            c.tags as tags
    FROM Enrollment e
    JOIN Course c ON c.id = e.courseId
    JOIN User u ON u.id = c.instructorId
    WHERE e.userId = :userId
    """)
    List<EnrollmentSummaryView> projectEnrollmentSummaries(@Param("userId") Long userId);
}
