package com.lxpbe.course.domain;

import com.lxpbe.course.domain.enums.Level;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "course",  indexes = {
        @Index(name = "idx_course_uuid", columnList = "uuid")
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    @Getter
    @Column(nullable = false)
    private String title;

    @Getter
    private String description;

    @Getter
    private String thumbnailUrl;

    @Getter
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private List<Section> sections = new ArrayList<>();

    @Getter
    @Enumerated(EnumType.STRING)
    private Level difficulty;

    @Getter
    @ElementCollection
    @CollectionTable(
            name = "course_tags",
            joinColumns = @JoinColumn(name = "course_id"),
            indexes = @Index(name = "idx_course_tags_tag_id", columnList = "tag_id")
    )
    @Column(name = "tag_id")
    @OrderColumn(name = "tag_order")
    private List<Long> tags = new ArrayList<>();

    @Builder
    public Course(Long instructorId, String title,
                           String description, String thumbnailUrl,
                           Level difficulty, List<Long> tags) {
        this.instructorId = instructorId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.difficulty = difficulty;
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.assignCourse(this);
    }
}
