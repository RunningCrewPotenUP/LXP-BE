package com.lxpbe.tag.presentation.response;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.domain.enums.TagStatus;
import lombok.Builder;

import java.util.Objects;

@Builder
public record TagResponse(
        long tagId,
        String name,
        String category,
        String subCategory,
        TagStatus status // ACTIVE, INACTIVE
) {
    public static TagResponse of(Tag tag) {
        Objects.requireNonNull(tag, "TagResponse 생성 실패: tag 가 null 입니다.");

        return TagResponse.builder()
                .tagId(tag.id())
                .name(tag.name())
                .category(tag.category())
                .subCategory(tag.subCategory())
                .status(tag.status())
                .build();
    }
}
