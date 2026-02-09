package com.lxpbe.course.repository;

import com.lxpbe.course.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c FROM Course c
        WHERE (:keyword IS NULL OR :keyword = ''
            OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<Course> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.sections WHERE c.id = :id")
    Optional<Course> findByIdWithSectionsAndLectures(@Param("id") Long id);
}
