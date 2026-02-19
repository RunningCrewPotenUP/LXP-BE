package com.recommend.infrastructure.adapter;

import com.lxpbe.course.infrastructure.facade.CourseFacade;
import com.lxpbe.course.infrastructure.facade.dto.CourseMetadataDto;
import com.recommend.application.dto.CourseMetaData;
import com.recommend.application.port.required.CourseMetaQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Course BC Adapter
 * - CourseMetaQueryPort 구현
 * - CourseFacade를 통해 강좌 메타데이터 조회
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CourseAdapter implements CourseMetaQueryPort {

    private final CourseFacade courseFacade;  // ✅ Course BC Facade 주입

    /**
     * 전체 공개 강좌 조회
     *
     * @return 공개 강좌 메타데이터 리스트
     */
    @Override
    public List<CourseMetaData> findAll() {
        log.debug("[CourseAdapter] Fetching all public courses");

        try {
            // 1. CourseFacade 호출
            List<CourseMetadataDto> dtos = courseFacade.findAllPublicCourses();

            // 2. Facade DTO → Application DTO 변환
            return dtos.stream()
                    .map(this::toApplicationDto)
                    .toList();

        } catch (Exception e) {
            log.error("[CourseAdapter] Failed to fetch public courses", e);
            return List.of();  // ✅ 예외 발생 시 빈 리스트 반환
        }
    }

    /**
     * 난이도별 강좌 조회
     *
     * @param targetDifficulties 대상 난이도 Set
     * @param limit 최대 조회 개수
     * @return 필터링된 강좌 메타데이터 리스트
     */
    @Override
    public List<CourseMetaData> findByDifficulties(Set<String> targetDifficulties, int limit) {
        log.debug("[CourseAdapter] Fetching courses by difficulties={}, limit={}",
                targetDifficulties, limit);

        try {
            // 1. 전체 공개 강좌 조회
            List<CourseMetadataDto> dtos = courseFacade.findAllPublicCourses();

            // 2. 난이도 필터링 + 제한
            return dtos.stream()
                    .filter(dto -> targetDifficulties.contains(dto.difficulty()))
                    .limit(limit)
                    .map(this::toApplicationDto)
                    .toList();

        } catch (Exception e) {
            log.error("[CourseAdapter] Failed to fetch courses by difficulties", e);
            return List.of();
        }
    }

    /**
     * 강좌 ID 목록으로 조회
     *
     * @param courseIds 강좌 ID 리스트
     * @return 강좌 메타데이터 리스트
     */
    @Override
    public List<CourseMetaData> findByCourses(List<Long> courseIds) {
        log.debug("[CourseAdapter] Fetching courses by IDs={}", courseIds);

        try {
            // 1. CourseFacade 호출
            List<CourseMetadataDto> dtos = courseFacade.findByIds(courseIds);

            // 2. Facade DTO → Application DTO 변환
            return dtos.stream()
                    .map(this::toApplicationDto)
                    .toList();

        } catch (Exception e) {
            log.error("[CourseAdapter] Failed to fetch courses by IDs", e);
            return List.of();
        }
    }

    /**
     * CourseMetadataDto → CourseMetaData 변환
     */
    private CourseMetaData toApplicationDto(CourseMetadataDto dto) {
        return new CourseMetaData(
                dto.courseId(),
                dto.title(),
                dto.tags(),       // Set<String> 그대로 전달
                dto.difficulty(), // String 그대로 전달
                dto.isPublic()
        );
    }
}
