package com.lxpbe.course.infrastructure.adapter;

import com.lxpbe.course.application.repository.CourseRepository;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.infrastructure.repository.CourseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseRepository {
    private final CourseJpaRepository courseJpaRepository;

    @Override
    public Page<Course> searchByKeyword(String keyword, Pageable pageable) {
        return courseJpaRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseJpaRepository.findByIdWithSectionsAndLectures(id);
    }

    @Override
    public Course save(Course course) {
        return courseJpaRepository.save(course);
    }

    @Override
    public void delete(Course course) {
        courseJpaRepository.delete(course);
    }
}
