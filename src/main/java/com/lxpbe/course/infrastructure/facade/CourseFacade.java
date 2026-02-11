package com.lxpbe.course.infrastructure.facade;

import com.lxpbe.course.application.result.CourseResult;
import com.lxpbe.course.application.service.CourseQueryService;
import com.lxpbe.course.infrastructure.facade.dto.CourseMetadataDto;
import com.lxpbe.tag.application.TagQueryService;
import com.lxpbe.tag.application.result.TagResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseFacade {
    private final CourseQueryService courseQueryService;
    private final TagQueryService tagQueryService;

    public List<CourseMetadataDto> findAllPublicCourses() {
        List<CourseResult> courses = courseQueryService.findAllPublicCourses();
        return courses.stream().map(course -> {
          List<TagResult> tags = tagQueryService.findByIds(course.tags());
          return CourseMetadataDto.from(course, tags);
        }).toList();
    }

    public List<CourseMetadataDto> findByIds(List<Long> courseIds) {
        List<CourseResult> courses = courseQueryService.findById(courseIds);
        return courses.stream().map(course -> {
            List<TagResult> tags = tagQueryService.findByIds(course.tags());
            return CourseMetadataDto.from(course, tags);
        }).toList();
    }
}
