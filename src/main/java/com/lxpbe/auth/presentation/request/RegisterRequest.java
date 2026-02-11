package com.lxpbe.auth.presentation.request;

import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RegisterRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Schema(example = "user@example.com")
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        @Pattern(regexp = PASSWORD_REGEX,
                message = "비밀번호는 영어 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함하여 8~20자여야 합니다.")
        @Schema(example = "Password1!")
        String password,

        @NotBlank(message = "이름은 비어있을 수 없습니다.")
        @Schema(example = "홍길동")
        String name,

        @NotNull(message = "역할은 비어있을 수 없습니다.")
        @Schema(example = "LEARNER")
        Role role,

        @NotNull(message = "태그는 비어있을 수 없습니다.")
        @Size(min = 3, max = 5, message = "태그는 3개 이상 5개 이하로 입력해야 합니다.")
        @Schema(example = "[1, 2, 3]")
        List<Long> tagIds,

        @NotNull(message = "레벨은 비어있을 수 없습니다.")
        @Schema(example = "JUNIOR")
        Level level
) {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
}
