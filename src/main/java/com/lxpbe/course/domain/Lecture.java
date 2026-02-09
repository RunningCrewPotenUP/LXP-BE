package com.lxpbe.course.domain;

import com.lxpbe.course.application.command.LectureCreateCommand;
import com.lxpbe.course.application.command.LectureUpdateCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "lecture")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    private String title;

    @Getter
    private Long durationSeconds;

    @Getter
    @Column(name = "sort_order")
    private int order;

    @Getter
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    private Lecture(String title, Long durationSeconds, int order, String videoUrl) {
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.order = order;
        this.videoUrl = videoUrl;
    }

    public static Lecture create(LectureCreateCommand command, int order) {
        return new Lecture(command.title(), command.durationSeconds(), order, command.videoUrl());
    }

    public static Lecture create(LectureUpdateCommand command, int order) {
        return new Lecture(command.title(), command.durationSeconds(), order, command.videoUrl());
    }

    public void assignSection(Section section) {
        this.section = section;
    }

    public void update(LectureUpdateCommand command) {
        if (command.title() != null) {
            this.title = command.title();
        }
        if (command.videoUrl() != null) {
            this.videoUrl = command.videoUrl();
        }
        if (command.durationSeconds() != null) {
            this.durationSeconds = command.durationSeconds();
        }
    }

    public void updateOrder(int order) {
        this.order = order;
    }
}
