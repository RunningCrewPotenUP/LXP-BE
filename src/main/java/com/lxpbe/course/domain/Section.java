package com.lxpbe.course.domain;

import com.lxpbe.course.application.command.LectureUpdateCommand;
import com.lxpbe.course.application.command.SectionCreateCommand;
import com.lxpbe.course.application.command.SectionUpdateCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Table(name = "section")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Column(nullable = false)
    private String title;

    @Getter
    @Column(name = "sort_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Getter
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @BatchSize(size = 10)
    private List<Lecture> lectures = new ArrayList<>();

    private Section(String title, int order) {
        this.title = title;
        this.order = order;
    }

    public static Section create(SectionCreateCommand command, int order) {
        Section section = new Section(command.title(), order);
        if (command.lectures() != null) {
            AtomicInteger lectureOrder = new AtomicInteger(1);
            command.lectures().forEach(lectureCommand -> {
                Lecture lecture = Lecture.create(lectureCommand, lectureOrder.getAndIncrement());
                section.addLecture(lecture);
            });
        }
        return section;
    }

    public static Section create(SectionUpdateCommand command, int order) {
        Section section = new Section(command.title(), order);
        if (command.lectures() != null) {
            AtomicInteger lectureOrder = new AtomicInteger(1);
            command.lectures().forEach(lectureCommand -> {
                Lecture lecture = Lecture.create(lectureCommand, lectureOrder.getAndIncrement());
                section.addLecture(lecture);
            });
        }
        return section;
    }

    public void update(SectionUpdateCommand command) {
        if (command.title() != null) {
            this.title = command.title();
        }

        if (command.lectures() != null) {
            Set<Long> commandLectureIds = command.lectures().stream()
                    .map(LectureUpdateCommand::id)
                    .filter(id -> id != null)
                    .collect(Collectors.toSet());

            this.lectures.removeIf(lecture -> !commandLectureIds.contains(lecture.getId()));

            AtomicInteger lectureOrder = new AtomicInteger(1);
            command.lectures().forEach(lectureCommand -> {
                if (lectureCommand.id() != null) {
                    this.lectures.stream()
                            .filter(lecture -> lecture.getId().equals(lectureCommand.id()))
                            .findFirst()
                            .ifPresent(lecture -> {
                                lecture.update(lectureCommand);
                                lecture.updateOrder(lectureOrder.getAndIncrement());
                            });
                } else {
                    Lecture newLecture = Lecture.create(lectureCommand, lectureOrder.getAndIncrement());
                    this.addLecture(newLecture);
                }
            });
        }
    }

    public void assignCourse(Course course) {
        this.course = course;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.assignSection(this);
    }

    public void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateOrder(int order) {
        this.order = order;
    }

    public int getTotalDurationSeconds() {
        return this.lectures.stream()
                .mapToInt(lecture -> lecture.getDurationSeconds() != null ? lecture.getDurationSeconds().intValue() : 0)
                .sum();
    }
}
