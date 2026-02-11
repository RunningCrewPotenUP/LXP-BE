package com.lxpbe.auth.presentation.controller;

import com.lxpbe.auth.application.dto.LoginDto;
import com.lxpbe.auth.application.dto.RegisterDto;
import com.lxpbe.auth.application.service.AuthService;
import com.lxpbe.auth.presentation.request.LoginRequest;
import com.lxpbe.auth.presentation.request.RegisterRequest;
import com.lxpbe.common.security.CookieProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final CookieProvider cookieProvider;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(RegisterDto.from(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String accessToken = authService.login(LoginDto.from(request));
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        cookieProvider.createAccessTokenCookie(accessToken).toString()
                )
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        cookieProvider.deleteAccessTokenCookie().toString()
                )
                .build();
    }
}
