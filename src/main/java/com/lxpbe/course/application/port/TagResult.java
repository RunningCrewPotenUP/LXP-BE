package com.lxpbe.course.application.port;

public record TagResult(
        Long id,
        String content,
        String color,
        String variant
) {
    public static TagResult unknown(Long id) {
        return new TagResult(id, "알 수 없음", "gray", "SOLID");
    }
}
