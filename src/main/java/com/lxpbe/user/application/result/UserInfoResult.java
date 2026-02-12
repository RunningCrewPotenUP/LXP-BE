package com.lxpbe.user.application.result;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.user.domain.User;
import java.util.List;

public record UserInfoResult(
        Long userId,
        String email,
        String name,
        String role,
        List<TagInfo> tags,
        String level
) {

    public record TagInfo(Long id, String content) {
    }

    public static UserInfoResult from(User user, List<Tag> tags) {
        List<TagInfo> tagInfos = tags.stream()
                .map(tag -> new TagInfo(tag.id(), tag.name()))
                .toList();

        return new UserInfoResult(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                tagInfos,
                user.getLevel().name()
        );
    }
}
