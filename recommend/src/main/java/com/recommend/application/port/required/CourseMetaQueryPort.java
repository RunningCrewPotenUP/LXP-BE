package com.recommend.application.port.required;

import com.recommend.application.dto.CourseMetaData;

import java.util.List;
import java.util.Set;

public interface CourseMetaQueryPort {

    /**
     * 공개된 모든 강좌 조회
     */
    List<CourseMetaData> findAll();

    /**
     * 특정 난이도의 강좌 조회
     */
    List<CourseMetaData> findByDifficulties(Set<String> targetDifficulties, int limit);

    /**
     * 강좌 ID 목록으로 조회
     */
    List<CourseMetaData> findByCourses(List<Long> courseIds);  // ✅ 메서드명 유지, 타입만 Long
}
