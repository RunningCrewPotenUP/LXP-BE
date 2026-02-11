package com.lxpbe.tag.application.result;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.domain.enums.TagStatus;
import lombok.Builder;

import java.util.Objects;

@Builder
public record TagResult(
        long tagId,
        String name,
        String category,
        String subCategory,
        TagStatus status
) {
    public static TagResult of(Tag tag) {
        Objects.requireNonNull(tag, "TagResponse 생성 실패: tag 가 null 입니다.");

        return TagResult.builder()
                .tagId(tag.id())
                .name(tag.name())
                .category(tag.category())
                .subCategory(tag.subCategory())
                .status(tag.status())
                .build();
    }
}
