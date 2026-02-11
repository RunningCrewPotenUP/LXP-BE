package com.lxpbe.user.presentation.response;

import com.lxpbe.user.application.result.UserInfoResult;
import com.lxpbe.user.application.result.UserInfoResult.TagInfo;
import java.util.List;

public record UserInfoResponse(
        Long userId,
        String email,
        String name,
        List<String> roles,
        List<TagInfo> tags,
        String level
) {

    public static UserInfoResponse from(UserInfoResult result) {
        return new UserInfoResponse(
                result.userId(),
                result.email(),
                result.name(),
                result.roles(),
                result.tags(),
                result.level()
        );
    }
}
