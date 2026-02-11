package com.lxpbe.course.application.repository;

import com.lxpbe.course.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseRepository {
    Page<Course> searchByKeyword(String keyword, Pageable pageable);
    Optional<Course> findById(Long id);
    Optional<Course> findByInstructorId(Long courseId, Long instructorId);
    Course save(Course course);
    void delete(Course course);
}
