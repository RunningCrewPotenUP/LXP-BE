package com.lxpbe.auth.presentation.request;

import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record RegisterRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        String email,
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        @Pattern(regexp = PASSWORD_REGEX,
                message = "비밀번호는 영어 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함하여 8~20자여야 합니다.")
        String password,
        @NotBlank(message = "이름은 비어있을 수 없습니다.")
        String name,
        @NotNull(message = "역할은 비어있을 수 없습니다.")
        Role role,
        @NotNull(message = "태그는 비어있을 수 없습니다.")
        List<Long> tagIds,
        @NotNull(message = "레벨은 비어있을 수 없습니다.")
        Level level
) {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
}
