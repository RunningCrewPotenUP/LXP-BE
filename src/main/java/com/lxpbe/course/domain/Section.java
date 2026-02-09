package com.lxpbe.course.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private List<Lecture> lectures = new ArrayList<>();

    @Builder
    public Section(String uuid, String title, int order) {
        this.title = title;
        this.order = order;
    }

    public void assignCourse(Course course) {
        this.course = course;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.assignSection(this);
    }
}
