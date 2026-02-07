package com.lxpbe.common.exception;

public record ErrorBody(
        String code,
        String message
) {
}
