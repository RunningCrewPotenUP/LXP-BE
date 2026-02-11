package com.lxpbe.user.presentation.controller;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.user.presentation.docs.UserApiResponseExamples;
import com.lxpbe.user.presentation.request.UpdateUserInfoRequest;
import com.lxpbe.user.presentation.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "사용자 API")
public interface UserApi {

    @Operation(summary = "내 정보 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = UserApiResponseExamples.MY_INFO_200)))
    })
    ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(Long userId);

    @Operation(summary = "내 정보 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = UserApiResponseExamples.MY_INFO_200))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = UserApiResponseExamples.UPDATE_400)))
    })
    ResponseEntity<ApiResponse<UserInfoResponse>> updateMyInfo(Long userId, UpdateUserInfoRequest request);
}
