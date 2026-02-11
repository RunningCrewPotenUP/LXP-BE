package com.lxpbe.course.application.result;

import com.lxpbe.course.domain.Course;
import com.lxpbe.course.domain.enums.Level;

import java.util.List;

public record CourseResult(
        Long courseId,
        String title,
        Level difficulty,
        List<Long> tags
) {
    public static CourseResult from(Course course) {
        return new CourseResult(
                course.getId(),
                course.getTitle(),
                course.getDifficulty(),
                course.getTags()
        );
    }
}
