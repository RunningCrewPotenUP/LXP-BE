package com.recommend.domain.model.ids;

import java.util.Objects;

/**
 * 강좌 식별자 값 객체 (Value Object)
 * JPA 어노테이션 없음 - 순수 도메인 개념
 */
public class CourseId {

    private final Long value;  // 

    protected CourseId() {
        this.value = null; // JPA 재구성용
    }

    public CourseId(Long value) {  // ✅ Long 파라미터
        if (value == null) {
            throw new IllegalArgumentException("CourseId cannot be null");
        }
        this.value = value;
    }

    public static CourseId of(Long value) {  // ✅ Long 파라미터
        return new CourseId(value);
    }

    public Long getValue() {  // ✅ Long 반환
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseId courseId = (CourseId) o;
        return Objects.equals(value, courseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);  //  변환
    }
}
