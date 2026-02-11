package com.lxpbe.course.domain;

import com.lxpbe.course.application.command.CourseCreateCommand;
import com.lxpbe.course.application.command.CourseUpdateCommand;
import com.lxpbe.course.domain.enums.Level;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    public static Course create(Long instructorId, CourseCreateCommand command) {
        Course course = new Course(instructorId, command.title(), command.description(), command.thumbnailUrl(), command.level(), command.tags());

        if (command.sections() != null) {
            AtomicInteger sectionOrder = new AtomicInteger(1);
            command.sections().forEach(sectionCommand -> {
                Section section = Section.create(sectionCommand, sectionOrder.getAndIncrement());
                course.addSection(section);
            });
        }

        return course;
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

    public void update(CourseUpdateCommand command) {
        if (command.title() != null) {
            this.title = command.title();
        }
        if (command.description() != null) {
            this.description = command.description();
        }
        if (command.thumbnailUrl() != null) {
            this.thumbnailUrl = command.thumbnailUrl();
        }
        if (command.level() != null) {
            this.difficulty = command.level();
        }

        if (command.tags() != null) {
            List<Long> newTags = command.tags();

            this.tags.removeIf(tag -> !newTags.contains(tag));
            newTags.stream()
                    .filter(tag -> !this.tags.contains(tag))
                    .forEach(this.tags::add);
        }

        if (command.sections() != null) {
            Set<Long> commandSectionIds = command.sections().stream()
                    .map(s -> s.id())
                    .filter(id -> id != null)
                    .collect(Collectors.toSet());

            this.sections.removeIf(section -> !commandSectionIds.contains(section.getId()));

            AtomicInteger sectionOrder = new AtomicInteger(1);
            command.sections().forEach(sectionCommand -> {
                if (sectionCommand.id() != null) {
                    this.sections.stream()
                            .filter(section -> section.getId().equals(sectionCommand.id()))
                            .findFirst()
                            .ifPresent(section -> {
                                section.update(sectionCommand);
                                section.updateOrder(sectionOrder.getAndIncrement());
                            });
                } else {
                    Section newSection = Section.create(sectionCommand, sectionOrder.getAndIncrement());
                    this.addSection(newSection);
                }
            });
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
