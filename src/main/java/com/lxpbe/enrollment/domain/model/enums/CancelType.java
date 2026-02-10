package com.lxpbe.enrollment.domain.model.enums;

/**
 * 취소 유형
 *
 * 이번에 백오피스 고려좀 하라고 하셔서, 취소 관련 cs 및 운영 관점에서 필요한 취소 유형을 나눠봤습니다.
 * <br/><br/>
 * 취소 유형은 백오피스에서 취소 처리 주체와 경로를 파악하고 후속 작업을 결정하기 위해 활용될 것이라고 가정했습니다.
 * (물론 이러한 후속 작업들을 모두 구현할 수 있을 것 같지는 않습니다(ex. 환불 등).)
 * <br/><br/>
 * 각 취소 유형은 다음과 같습니다.
 * <ul>
 *     <li>SELF_SERVICE: 사용자 본인의 의사로, 사용자에 의해 직접 취소됨</li>
 *     <li>CS_SERVICE: 사용자가 고객센터에 문의하여, 상담사에 의해 취소됨</li>
 *     <li>ADMIN_PENALTY: 사용자의 정책 위반에 대한 제재 조치로서, 관리자에 의해 취소됨</li>
 *     <li>SYSTEM_COURSE_UNAVAILABLE: 강좌 폐강 등의 이유로 수강할 수 있는 상태가 아니어서, 시스템에 의해 취소됨</li>
 *     <li>SYSTEM_PAYMENT_REVERSAL: 결제 되돌리기(취소, 환불, 차지백, 승인 취소 등) 이벤트에 의해 취소됨</li>
 * </ul>
 */
public enum CancelType {
    SELF_SERVICE,
    CS_SERVICE,
    ADMIN_PENALTY,
    COURSE_UNAVAILABLE,
    PAYMENT_REVERSAL
}
