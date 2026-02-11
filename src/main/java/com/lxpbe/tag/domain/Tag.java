package com.lxpbe.tag.domain;

import com.lxpbe.tag.domain.enums.TagStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(
        name = "tag",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_tag_name", columnNames = "name"
        )
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Accessors(fluent = true)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String subCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagStatus status; // ACTIVE, INACTIVE

    @Builder
    public Tag(String name, String category, String subCategory, TagStatus status) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.status = status;
    }
}
