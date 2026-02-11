package com.lxpbe.auth.presentation.controller;

import com.lxpbe.auth.presentation.docs.AuthApiResponseExamples;
import com.lxpbe.auth.presentation.request.LoginRequest;
import com.lxpbe.auth.presentation.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = AuthApiResponseExamples.REGISTER_400))),
            @ApiResponse(responseCode = "409", description = "이메일 중복",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = AuthApiResponseExamples.REGISTER_409)))
    })
    ResponseEntity<Void> register(RegisterRequest request);

    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공 (Set-Cookie 헤더에 JWT 토큰 포함)"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = AuthApiResponseExamples.REGISTER_400))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = AuthApiResponseExamples.LOGIN_401)))
    })
    ResponseEntity<Void> login(LoginRequest request);

    @Operation(summary = "로그아웃")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공 (쿠키 삭제)")
    })
    ResponseEntity<Void> logout();
}
