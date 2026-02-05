package com.lxpbe.common.exception;

import java.time.Instant;

public record ErrorResponseBody(
        String code,
        String message,
        Instant timestamp
) {

    public ErrorResponseBody {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}
