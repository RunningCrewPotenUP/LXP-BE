package com.lxpbe.enrollment.domain.model;

import com.lxpbe.common.exception.ErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.domain.model.enums.CancelReasonType;
import com.lxpbe.enrollment.domain.model.enums.CancelType;
import com.lxpbe.enrollment.domain.model.enums.EnrollmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Enrollment 도메인 불변식 테스트")
class EnrollmentTest {

    @ParameterizedTest(name = "userId = {0}, courseId = {1}")
    @MethodSource("invalidIdParis")
    @DisplayName("Enrollment 엔티티 객체 생성 시 userId 또는 courseId 가 null 인 경우 EnrollmentException 발생")
    void shouldThrowsEnrollmentException_whenCreateEnrollment_givenRequiredFieldIsNull(
            Long userId, Long courseId, ErrorCode expectedErrorCode
    ) {

        var thrown = assertThrows(EnrollmentException.class, () -> {
            Enrollment.builder()
                    .userId(userId)
                    .courseId(courseId)
                    .build();
        });

        assertEquals(expectedErrorCode, thrown.errorCode());
    }
    static Stream<Arguments> invalidIdParis() {
        return Stream.of(
                Arguments.of(null, 1L, EnrollmentErrorCode.USER_ID_IS_REQUIRED_TO_CREATE_ENROLLMENT),
                Arguments.of(1L, null, EnrollmentErrorCode.COURSE_ID_IS_REQUIRED_TO_CREATE_ENROLLMENT)
        );
    }

    // ----- 취소 테스트

    @ParameterizedTest(name = "cancelType = {0}, cancelReasonType = {1}, cancelReasonComment = {2}")
    @MethodSource("invalidArgumentsForCancel")
    @DisplayName("취소 실패 테스트(유효하지 않은 파라미터)")
    void shouldThrowEnrollmentException_whenCancel_givenInvalidArgument(
            CancelType cancelType,
            CancelReasonType cancelReasonType,
            String cancelReasonComment,
            ErrorCode expectedErrorCode
    ) {

        var thrown = assertThrows(EnrollmentException.class, () -> {
            Enrollment enrollment = Enrollment.builder()
                    .userId(1L)
                    .courseId(1L)
                    .build();
            enrollment.cancel(cancelType, cancelReasonType, cancelReasonComment);
        });

        assertEquals(expectedErrorCode, thrown.errorCode());
    }
    static Stream<Arguments> invalidArgumentsForCancel() {
        return Stream.of(
                Arguments.of(null, CancelReasonType.TOO_EXPENSIVE, "취소 사유", EnrollmentErrorCode.CANCEL_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT),
                Arguments.of(CancelType.SELF_SERVICE, null, "취소 사유", EnrollmentErrorCode.CANCEL_REASON_TYPE_IS_REQUIRED_TO_CANCEL_ENROLLMENT),
                Arguments.of(CancelType.SELF_SERVICE, CancelReasonType.OTHER, null, EnrollmentErrorCode.CANCEL_REASON_COMMENT_MUST_NOT_BLANK_WHEN_CANCEL_REASON_IS_OTHER),
                Arguments.of(CancelType.SELF_SERVICE, CancelReasonType.OTHER, "", EnrollmentErrorCode.CANCEL_REASON_COMMENT_MUST_NOT_BLANK_WHEN_CANCEL_REASON_IS_OTHER),
                Arguments.of(CancelType.SELF_SERVICE, CancelReasonType.OTHER, " \t \n ", EnrollmentErrorCode.CANCEL_REASON_COMMENT_MUST_NOT_BLANK_WHEN_CANCEL_REASON_IS_OTHER)
        );
    }
    
    // ----- 상태 전환 테스트

    @Test
    @DisplayName("ENROLLED 상태일 때, startLearning 을 호출하면 IN_PROGRESS 상태로 변경된다.")
    void shouldChangeIntoInProgressStatus_whenStartLearning_givenEnrolledEnrollment() {
        Enrollment created = enrollmentEnrolled();
        created.startLearning();

        assertEquals(EnrollmentStatus.IN_PROGRESS, created.enrollmentStatus());
    }

    @ParameterizedTest(name = "{0} -> IN_PROGRESS")
    @MethodSource("enrollmentsWhichCannotBeTransitionIntoInProgress")
    @DisplayName("ENROLLED 상태가 아닐 때, startLearning 을 호출하면 EnrollmentException 이 발생한다.")
    void shouldThrowsEnrollmentException_whenStartLearning_givenEnrollmentIsNotEnrolled(
            EnrollmentStatus status, Enrollment enrollmentNotEnrolled, ErrorCode expectedErrorCode
    ) {

        var thrown = assertThrows(EnrollmentException.class, enrollmentNotEnrolled::startLearning);

        assertEquals(expectedErrorCode, thrown.errorCode());
    }
    static Stream<Arguments> enrollmentsWhichCannotBeTransitionIntoInProgress() {
        return Stream.of(
                Arguments.of(EnrollmentStatus.IN_PROGRESS, enrollmentInProgress(), EnrollmentErrorCode.ALREADY_IN_PROGRESS),
                Arguments.of(EnrollmentStatus.CANCELLED, enrollmentCancelled(), EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_IN_PROGRESS),
                Arguments.of(EnrollmentStatus.COMPLETED, enrollmentCompleted(), EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_IN_PROGRESS)
        );
    }

    @ParameterizedTest(name = "{0} -> CANCELLED")
    @MethodSource("cancellableEnrollments")
    @DisplayName("ENROLLED 또는 IN_PROGRESS 상태일 때, 유효한 인자로 cancel을 호출하면, 성공적으로 CANCELLED 상태로 변경된다.")
    void shouldChangeIntoCancelledStatus_whenCancel_givenEnrolledOrInProgressEnrollment(
            EnrollmentStatus status, Enrollment givenEnrollment
    ) {

        givenEnrollment.cancel(CancelType.SELF_SERVICE, CancelReasonType.TOO_EXPENSIVE, "취소 사유");

        assertEquals(EnrollmentStatus.CANCELLED, givenEnrollment.enrollmentStatus());
    }
    static Stream<Arguments> cancellableEnrollments() {
        return Stream.of(
                Arguments.of(EnrollmentStatus.ENROLLED, enrollmentEnrolled()),
                Arguments.of(EnrollmentStatus.IN_PROGRESS, enrollmentInProgress())
        );
    }

    @ParameterizedTest(name = "{0} -> CANCELLED")
    @MethodSource("uncancellableEnrollments")
    @DisplayName("CANCELLED 또는 COMPLETED 상태일 때, 유효한 인자로 cancel 을 호출하면, EnrollmentException 이 발생한다.")
    void shouldThrowsEnrollmentException_whenCancel_givenCancelledOrCompletedEnrollment(
            EnrollmentStatus status, Enrollment givenEnrollment, ErrorCode expectedErrorCode
    ) {

        var thrown = assertThrows(EnrollmentException.class, () -> {
            givenEnrollment.cancel(CancelType.SELF_SERVICE, CancelReasonType.TOO_EXPENSIVE, "취소 사유");
        });

        assertEquals(expectedErrorCode, thrown.errorCode());
    }
    static Stream<Arguments> uncancellableEnrollments() {
        return Stream.of(
                Arguments.of(EnrollmentStatus.CANCELLED, enrollmentCancelled(), EnrollmentErrorCode.ALREADY_CANCELLED),
                Arguments.of(EnrollmentStatus.COMPLETED, enrollmentCompleted(), EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_CANCELLED)
        );
    }

    @Test
    @DisplayName("IN_PROGRESS 상태일 때, complete 를 호출하면, COMPLETED 상태로 변경된다.")
    void shouldChangeIntoCompleted_whenComplete_givenInProgressEnrollment() {
        Enrollment enrollmentInProgress = enrollmentInProgress();
        enrollmentInProgress.complete();

        assertEquals(EnrollmentStatus.COMPLETED, enrollmentInProgress.enrollmentStatus());
    }

    @ParameterizedTest(name = "{0} -> COMPETED")
    @MethodSource("unCompletableEnrollments")
    @DisplayName("IN_PROGRESS 상태가 아닐 때, complete 를 호출하면, EnrollmentException 이 발생한다.")
    void shouldThrowsEnrollmentException_whenComplete_givenEnrollmentIsNotInProgress(
            EnrollmentStatus status, Enrollment givenEnrollment, ErrorCode expectedErrorCode
    ) {

        var thrown = assertThrows(EnrollmentException.class, () -> {
            givenEnrollment.complete();
        });

        assertEquals(expectedErrorCode, thrown.errorCode());
    }
    static Stream<Arguments> unCompletableEnrollments() {
        return Stream.of(
                Arguments.of(EnrollmentStatus.ENROLLED, enrollmentEnrolled(), EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_COMPLETED),
                Arguments.of(EnrollmentStatus.COMPLETED, enrollmentCompleted(), EnrollmentErrorCode.ALREADY_COMPLETED),
                Arguments.of(EnrollmentStatus.CANCELLED, enrollmentCancelled(), EnrollmentErrorCode.INVALID_STATUS_CHANGE_INTO_COMPLETED)
        );
    }

    // ----- helpers

    static Enrollment enrollmentEnrolled() {
        return Enrollment.builder()
                .userId(1L)
                .courseId(1L)
                .build();
    }

    static Enrollment enrollmentInProgress() {
        Enrollment enrollment = Enrollment.builder()
                .userId(1L)
                .courseId(1L)
                .build();
        enrollment.startLearning();
        return enrollment;
    }

    static Enrollment enrollmentCancelled() {
        Enrollment enrollment = Enrollment.builder()
                .userId(1L)
                .courseId(1L)
                .build();
        enrollment.cancel(
                CancelType.SELF_SERVICE,
                CancelReasonType.TOO_EXPENSIVE,
                "다시 생각해보니까 너무 비싸요"
        );
        return enrollment;
    }

    static Enrollment enrollmentCompleted() {
        Enrollment enrollment = Enrollment.builder()
                .userId(1L)
                .courseId(1L)
                .build();
        enrollment.startLearning();
        enrollment.complete();
        return enrollment;
    }
}