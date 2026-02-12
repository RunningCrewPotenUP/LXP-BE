package com.lxpbe.user.presentation.response;

import com.lxpbe.user.application.result.UserInfoResult;
import com.lxpbe.user.application.result.UserInfoResult.TagInfo;

import java.util.List;

public record UserInfoResponse(
        Long userId,
        String email,
        String name,
        String role,
        List<TagInfo> tags,
        String level
) {

    public static UserInfoResponse from(UserInfoResult result) {
        return new UserInfoResponse(
                result.userId(),
                result.email(),
                result.name(),
                result.role(),
                result.tags(),
                result.level()
        );
    }
}
