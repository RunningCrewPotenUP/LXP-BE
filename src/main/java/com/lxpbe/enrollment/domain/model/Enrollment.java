package com.lxpbe.enrollment.domain.model;

import com.lxpbe.enrollment.domain.event.EnrollmentCancelled;
import com.lxpbe.enrollment.domain.event.EnrollmentCompleted;
import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;

import static com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode.CANCEL_REASON_COMMENT_MUST_NOT_BLANK_WHEN_CANCEL_REASON_IS_OTHER;
import static com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode.CANCEL_REASON_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT;
import static com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode.CANCEL_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT;

@Entity
@Table(
        name = "enrollment",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_id_course_id",
                columnNames = {"user_id", "course_id", "cancelledAt"}
        ),
        indexes = @Index(
                name = "idx_course_id",
                columnList = "course_id"
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Accessors(fluent = true)
public class Enrollment extends AbstractAggregateRoot<Enrollment> {

    // ----- 낙관적 락 적용

    @Version
    private Integer version;

    // ----- fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private Long courseId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;

    @Column(nullable = false)
    private Instant enrolledAt;

    private Instant learningStartedAt;

    private Instant completedAt;

    private Instant deletedAt;

    private Instant cancelledAt;

    @Enumerated(EnumType.STRING)
    private CancelType cancelType;

    @Enumerated(EnumType.STRING)
    private CancelReasonType cancelReasonType;

    private String cancelReasonComment;

    // ----- constructors

    @Builder
    private Enrollment(Long userId, Long courseId) {

        if (userId == null) {
            throw new EnrollmentException(EnrollmentErrorCode.USER_ID_IS_REQUIRED_TO_CREATE_ENROLLMENT);
        }

        if (courseId == null) {
            throw new EnrollmentException(EnrollmentErrorCode.COURSE_ID_IS_REQUIRED_TO_CREATE_ENROLLMENT);
        }

        this.userId = userId;
        this.courseId = courseId;

        enrollmentStatus = EnrollmentStatus.ENROLLED;

        enrolledAt = Instant.now();
        learningStartedAt = null;
        completedAt = null;
        deletedAt = null;

        cancelledAt = null;
        cancelType = null;
        cancelReasonType = null;
        cancelReasonComment = null;

        // To Do: 수강 생성 시 EnrollmentCreated 이벤트 발행해야 함(현재는 자동 생성 키라서 생성자에서 등록 불가)
    }

    // ----- domain logics

    public void startLearning() {
        learningStartedAt = Instant.now();
        enrollmentStatus = enrollmentStatus.toInProgress();

        validateDatesOrder();
    }

    public void cancel(
            CancelType cancelType,
            CancelReasonType cancelReasonType,
            String cancelReasonComment
    ) {

        // To Do: 정책적인 취소 가능 여부에 대해서는 서비스 계층에서 수행된다고 가정

        if (cancelType == null) {
            throw new EnrollmentException(CANCEL_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT);
        }

        if (cancelReasonType == null) {
            throw new EnrollmentException(CANCEL_REASON_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT);
        }

        if (cancelReasonType.equals(CancelReasonType.OTHER)
                && (cancelReasonComment == null || cancelReasonComment.isBlank())) {
            throw new EnrollmentException(CANCEL_REASON_COMMENT_MUST_NOT_BLANK_WHEN_CANCEL_REASON_IS_OTHER);
        }

        this.cancelledAt = Instant.now();
        this.cancelType = cancelType;
        this.cancelReasonType = cancelReasonType;
        this.cancelReasonComment = cancelReasonComment;
        this.enrollmentStatus = enrollmentStatus.toCancelled();

        validateDatesOrder();

        registerCancelEvent();
    }

    public void complete() {
        completedAt = Instant.now();
        enrollmentStatus = enrollmentStatus.toCompleted();

        validateDatesOrder();

        registerCompleteEvent();
    }

    // ----- validators

    private void validateDatesOrder() {
        if ((learningStartedAt != null && enrolledAt.isAfter(learningStartedAt))
                || (deletedAt != null && enrolledAt.isAfter(deletedAt))
                || (cancelledAt != null && enrolledAt.isAfter(cancelledAt))
                || (completedAt != null && enrolledAt.isAfter(completedAt))
                || (learningStartedAt != null && completedAt != null && learningStartedAt.isAfter(completedAt))
                || (cancelledAt != null && completedAt != null)
        ) {
            throw new EnrollmentException(EnrollmentErrorCode.DATE_ORDER_CONSISTENCY_HAS_BROKEN);
        }
    }

    // ----- event helpers

    private void registerCancelEvent() {

        if (cancelledAt == null
                || cancelType == null
                || cancelReasonType == null
                || !enrollmentStatus.equals(EnrollmentStatus.CANCELLED)
        ) {
            throw new IllegalStateException("취소 상태가 아니거나, cancelledAt, cancelType, cancelReasonType 중 하나 이상이 null 이어서 취소 이벤트를 등록할 수 없습니다.");
        }

        this.registerEvent(EnrollmentCancelled.builder()
                .enrollmentId(this.id)
                .userId(this.userId)
                .courseId(this.courseId)
                .cancelledAt(this.cancelledAt)
                .cancelType(this.cancelType)
                .cancelReasonType(this.cancelReasonType)
                .cancelReasonComment(this.cancelReasonComment)
                .build()
        );
    }

    private void registerCompleteEvent() {

        if (!enrollmentStatus.equals(EnrollmentStatus.COMPLETED) || completedAt == null) {
            throw new IllegalStateException("completedAt 이 null 이거나 수강이 완료 상태가 아닌 경우 완료 이벤트를 등록할 수 없습니다.");
        }

        this.registerEvent(EnrollmentCompleted.builder()
                .enrollmentId(this.id)
                .userId(this.userId)
                .courseId(this.courseId)
                .completedAt(this.completedAt)
        );
    }
}
