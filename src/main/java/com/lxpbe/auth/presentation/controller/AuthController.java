package com.lxpbe.auth.presentation.controller;

import com.lxpbe.auth.application.dto.LoginDto;
import com.lxpbe.auth.application.dto.RegisterDto;
import com.lxpbe.auth.application.service.AuthService;
import com.lxpbe.auth.presentation.request.LoginRequest;
import com.lxpbe.auth.presentation.request.RegisterRequest;
import com.lxpbe.auth.presentation.response.TokenResponse;
import com.lxpbe.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        authService.register(RegisterDto.from(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(LoginDto.from(request));
        return ResponseEntity.ok(new ApiResponse<>(tokenResponse, null));
    }
}
