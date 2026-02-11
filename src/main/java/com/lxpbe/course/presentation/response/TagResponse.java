package com.lxpbe.course.presentation.response;

import com.lxpbe.tag.application.result.TagResult;

public record TagResponse(
        long id,
        String content
) {
    public static TagResponse from(TagResult result) {
        return new TagResponse(result.tagId(), result.name());
    }
}
