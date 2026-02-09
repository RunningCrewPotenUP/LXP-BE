package com.lxpbe.course.domain;

import com.lxpbe.course.domain.enums.Level;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Table(name = "course")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    @Getter
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Getter
    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

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
    @BatchSize(size = 10)
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

    private Course(Long instructorId, String title,
                           String description, String thumbnailUrl,
                           Level difficulty, List<Long> tags) {
        this.instructorId = instructorId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.difficulty = difficulty;
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public static Course create(
            Long instructorId,
            String title,
            String description,
            String thumbnailUrl,
            Level difficulty,
            List<Long> tags
    ) {
        return new Course(instructorId, title, description, thumbnailUrl, difficulty, tags);
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.assignCourse(this);
    }

    public void updateBasicInfo(String title, String description, String thumbnailUrl, Level difficulty) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (thumbnailUrl != null) {
            this.thumbnailUrl = thumbnailUrl;
        }
        if (difficulty != null) {
            this.difficulty = difficulty;
        }
    }

    public void updateTags(List<Long> tags) {
        this.tags.clear();
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public void clearSections() {
        this.sections.clear();
    }

    public int getTotalDurationSeconds() {
        return this.sections.stream()
                .mapToInt(Section::getTotalDurationSeconds)
                .sum();
    }

    public int getTotalDurationHours() {
        int totalSeconds = getTotalDurationSeconds();
        return (int) Math.ceil(totalSeconds / 3600.0);
    }
}
