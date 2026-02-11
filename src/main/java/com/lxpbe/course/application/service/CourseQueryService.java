package com.lxpbe.course.application.service;

import com.lxpbe.course.application.result.CourseResult;
import com.lxpbe.course.infrastructure.repository.CourseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CourseQueryService {
    private final CourseJpaRepository courseJpaRepository;

    public List<CourseResult> findAllPublicCourses() {
        return courseJpaRepository.findAll().stream()
                .map(CourseResult::from)
                .toList();
    }

    public List<CourseResult> findById(List<Long> courseIds) {
        return courseJpaRepository.findAllById(courseIds).stream()
                .map(CourseResult::from)
                .toList();
    }
}
