package com.lxpbe.enrollment.domain.model.enums;

/**
 * 취소 사유 유형
 *
 * 이번에 백오피스 고려 하라고 하셔서, 취소 관련 cs 및 운영 관점에서 필요한 취소 사유 유형을 나눠봤습니다.
 * <br/><br/>
 * 취소 사유 유형은 사내 의사결정(정책 결정 등)을 위한 통계 자료로서 활용되거나, 맞춤 고객 응대 등에 활용될 수 있습니다.
 * <br/><br/>
 * 각 취소 사유 유형은 다음과 같습니다.
 * <ul>
 *     <li>PURCHASE_MISTAKE: 착오로 인한 취소</li>
 *     <li>BETTER_ALTERNATIVE_EXISTS: 더 좋은 다른 강의를 듣기 위함</li>
 *     <li>PLATFORM_INCONVENIENT: 서비스 플랫폼 사용이 불편함</li>
 *     <li>NO_LONGER_NEEDED: 수강할 필요가 없어짐</li>
 *     <li>TOO_EXPENSIVE: 강의 품질 대비 너무 비쌈</li>
 *     <li>POOR_QUALITY: 강의 품질 불만</li>
 *     <li>CONTENT_NOT_AS_EXPECTED: 강의 내용이 예상과 다름</li>
 *     <li>TECHNICAL_ISSUE: 기술적 문제(ex. 접속 장애, 재생 문제, 인증 실패, 결제 실패 등)</li>
 *     <li>POLICY_VIOLATION: 사용자 정책 위반</li>
 *     <li>FRAUD_SUSPECTED: 부정 결제(이상 거래) 또는 도용 등 부정 사용 의심</li>
 *     <li>OTHER: 그 외(상세 메모 필수)</li>
 */
public enum CancelReasonType {
    PURCHASE_MISTAKE,
    BETTER_ALTERNATIVE_EXISTS,
    PLATFORM_INCONVENIENT,
    NO_LONGER_NEEDED,
    TOO_EXPENSIVE,
    POOR_QUALITY,
    CONTENT_NOT_AS_EXPECTED,
    TECHNICAL_ISSUE,
    POLICY_VIOLATION,
    FRAUD_SUSPECTED,
    OTHER
}
