package com.lxpbe.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public Lecture(String title, Long durationSeconds, int order, String videoUrl) {
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.order = order;
        this.videoUrl = videoUrl;
    }

    public void assignSection(Section section) {
        this.section = section;
    }

    public void updateInfo(String title, String videoUrl) {
        if (title != null) {
            this.title = title;
        }
        if (videoUrl != null) {
            this.videoUrl = videoUrl;
        }
    }

    public void updateDuration(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void updateOrder(int order) {
        this.order = order;
    }
}
