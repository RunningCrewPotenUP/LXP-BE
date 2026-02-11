package com.lxpbe.user.presentation.controller;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.common.security.LoginUser;
import com.lxpbe.user.application.result.UserInfoResult;
import com.lxpbe.user.application.service.UserCommandService;
import com.lxpbe.user.application.service.UserQueryService;
import com.lxpbe.user.presentation.request.UpdateUserInfoRequest;
import com.lxpbe.user.presentation.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(@LoginUser Long userId) {
        UserInfoResult result = userQueryService.getMyInfo(userId);
        UserInfoResponse response = UserInfoResponse.from(result);
        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateMyInfo(
            @LoginUser Long userId,
            @RequestBody UpdateUserInfoRequest request) {
        UserInfoResult result = userCommandService.updateMyInfo(
                userId, request.name(), request.tagIds(), request.level());
        UserInfoResponse response = UserInfoResponse.from(result);
        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }
}
