package com.lxpbe.auth.application.dto;

import com.lxpbe.auth.presentation.request.LoginRequest;

public record LoginDto(
        String email,
        String password
) {

    public static LoginDto from(LoginRequest request) {
        return new LoginDto(request.email(), request.password());
    }
}
