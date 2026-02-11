package com.recommend.infrastructure.adapter;

import com.recommend.application.dto.CourseMetaData;
import com.recommend.application.port.required.CourseMetaQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
//이제 사용 안함! 대체
/**
 * Course BC 연동 Adapter
 * - HTTP 호출 제거, 로컬 Service 직접 호출
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CourseMetaAdapter implements CourseMetaQueryPort {

    // TODO: Course BC의 실제 Service 주입
    // private final CourseQueryService courseQueryService;

    @Override
    public List<CourseMetaData> findAll() {
        log.debug("[CourseMetaAdapter] 전체 강좌 조회");

        // TODO: Course BC Service 호출로 교체
        // List<Course> courses = courseQueryService.findAllPublishedCourses();
        // return courses.stream()
        //     .map(this::toCourseMetaData)
        //     .toList();

        throw new UnsupportedOperationException("Course BC Service 연동 필요");
    }

    @Override
    public List<CourseMetaData> findByDifficulties(Set<String> targetDifficulties, int limit) {
        log.debug("[CourseMetaAdapter] 난이도별 강좌 조회: difficulties={}, limit={}",
                targetDifficulties, limit);

        // TODO: Course BC Service 호출로 교체
        // List<Course> courses = courseQueryService.findByDifficulties(targetDifficulties, limit);
        // return courses.stream()
        //     .map(this::toCourseMetaData)
        //     .toList();

        throw new UnsupportedOperationException("Course BC Service 연동 필요");
    }

    @Override
    public List<CourseMetaData> findByCourses(List<Long> courseIds) {
        log.debug("[CourseMetaAdapter] 강좌 ID 목록 조회: courseIds={}", courseIds);

        // TODO: Course BC Service 호출로 교체
        // List<Course> courses = courseQueryService.findByIds(courseIds);
        // return courses.stream()
        //     .map(this::toCourseMetaData)
        //     .toList();

        throw new UnsupportedOperationException("Course BC Service 연동 필요");
    }

    /**
     * Course Domain Model → CourseMetaData DTO 변환
     * (Anti-Corruption Layer)
     */
    private CourseMetaData toCourseMetaData(Object course) {
        // TODO: Course BC의 실제 Domain Model로 변환
        // return new CourseMetaData(
        //     course.getId(),           // Long
        //     course.getTitle(),
        //     course.getTags(),
        //     course.getDifficulty(),
        //     course.isPublished()
        // );

        throw new UnsupportedOperationException("Course BC Domain Model 매핑 필요");
    }
}
