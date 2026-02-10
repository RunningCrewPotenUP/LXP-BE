package com.recommend.domain.model.ids;

import java.util.Objects;

/**
 * 회원 식별자 값 객체 (Value Object)
 * JPA 어노테이션 없음 - 순수 도메인 개념
 */
public class MemberId {

    private final Long value;  // ✅ String → Long

    protected MemberId() {
        this.value = null; // JPA 재구성용
    }

    public MemberId(Long value) {  // ✅ Long 파라미터
        if (value == null) {
            throw new IllegalArgumentException("MemberId cannot be null");
        }
        this.value = value;
    }

    public static MemberId of(Long value) {  // ✅ Long 파라미터
        return new MemberId(value);
    }

    public Long getValue() {  // ✅ Long 반환
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberId memberId = (MemberId) o;
        return Objects.equals(value, memberId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);  // ✅ Long → String 변환
    }
}
