package com.lxpbe.auth.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Schema(example = "user@example.com")
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        @Pattern(regexp = PASSWORD_REGEX,
                message = "비밀번호는 영어 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함하여 8~20자여야 합니다.")
        @Schema(example = "Password1!")
        String password
) {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
}
