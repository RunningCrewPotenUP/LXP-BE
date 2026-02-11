package com.lxpbe.user.presentation.request;

import com.lxpbe.user.domain.enums.Level;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record UpdateUserInfoRequest(
        @Schema(example = "홍길동")
        String name,

        @Schema(example = "[1, 2, 5]")
        List<Long> tagIds,

        @Schema(example = "JUNIOR")
        Level level
) {
}
