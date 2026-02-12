package com.lxpbe.tag.repository;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.repository.projection.CourseTagProjectionRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByIdIn(Collection<Long> ids);

    @Query(value = """
            select t.*
            from tag t
            join course_tags ct on t.id = ct.tag_id
            where ct.course_id = :courseId
            order by ct.tag_order
            """, nativeQuery = true)
    List<Tag> findAllByCourseId(@Param("courseId") Long courseId);

    @Query(value = """
            select
                ct.course_id as courseId,
                ct.tag_order as tagOrder,
                ct.tag_id as tagId,
                t.name as tagName,
                t.category as tagCategory,
                t.sub_category as tagSubCategory
            from course_tags ct
            join tag t on ct.tag_id = t.id
            where ct.course_id in (:courseIds)
            order by ct.course_id, ct.tag_order
            """, nativeQuery = true)
    List<CourseTagProjectionRow> findCourseTagInfoByCourseIds(@Param("courseIds") Collection<Long> courseIds);

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNameContaining(String name);
}
