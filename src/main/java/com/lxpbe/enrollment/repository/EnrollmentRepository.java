package com.lxpbe.enrollment.repository;

import com.lxpbe.enrollment.domain.model.Enrollment;
import com.lxpbe.enrollment.repository.projection.EnrollmentDetailsProjectionRow;
import com.lxpbe.enrollment.repository.projection.EnrollmentSummaryProjectionRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndCourseIdAndCancelledAtIsNull(Long userId, Long courseId);

    List<Enrollment> findAllByUserId(Long userId);

    Page<Enrollment> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
            select
                e.id as id,
                e.userId as userId,
                e.courseId as courseId,
                e.enrollmentStatus as status,
                e.enrolledAt as enrolledAt,
                e.learningStartedAt as learningStartedAt,
                e.cancelledAt as cancelledAt,
                e.cancelType as cancelType,
                e.cancelReasonType as reasonType,
                e.cancelReasonComment as reason,
                c.instructorId as instructorId,
                u.name as instructorName,
                c.thumbnailUrl as thumbnailUrl,
                c.title as courseTitle,
                c.description as courseDescription,
                c.difficulty as courseLevel
            from Enrollment e
            join Course c on c.id = e.courseId
            join User u on u.id = c.instructorId
            where e.id = :enrollmentId
            """)
    Optional<EnrollmentDetailsProjectionRow> findDetailsById(@Param("enrollmentId") Long enrollmentId);

    @Query(
            value = """
                    select
                        e.id as id,
                        e.courseId as courseId,
                        e.enrollmentStatus as status,
                        e.enrolledAt as enrolledAt,
                        e.learningStartedAt as learningStartedAt,
                        e.cancelledAt as cancelledAt,
                        e.cancelType as cancelType,
                        e.cancelReasonType as reasonType,
                        e.cancelReasonComment as reason,
                        c.instructorId as instructorId,
                        u.name as instructorName,
                        c.thumbnailUrl as thumbnailUrl,
                        c.title as courseTitle,
                        c.description as courseDescription,
                        c.difficulty as courseLevel
                    from Enrollment e, Course c, User u
                    where e.userId = :userId
                      and c.id = e.courseId
                      and u.id = c.instructorId
                    """,
            countQuery = """
                    select count(e.id)
                    from Enrollment e, Course c, User u
                    where e.userId = :userId
                      and c.id = e.courseId
                      and u.id = c.instructorId
                    """
    )
    Page<EnrollmentSummaryProjectionRow> findSummariesByUserId(@Param("userId") Long userId, Pageable pageable);
}
