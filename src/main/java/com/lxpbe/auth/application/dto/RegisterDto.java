package com.lxpbe.auth.application.dto;

import com.lxpbe.auth.presentation.request.RegisterRequest;
import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.domain.enums.Role;
import java.util.List;

public record RegisterDto(
        String email,
        String password,
        String name,
        Role role,
        List<Long> tagIds,
        Level level
) {

    public static RegisterDto from(RegisterRequest request) {
        return new RegisterDto(
                request.email(),
                request.password(),
                request.name(),
                request.role(),
                request.tagIds(),
                request.level()
        );
    }
}
