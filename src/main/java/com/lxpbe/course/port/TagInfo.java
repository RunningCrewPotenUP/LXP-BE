package com.lxpbe.course.port;

public record TagInfo(
        Long id,
        String content,
        String color,
        String variant
) {
    public static TagInfo unknown(Long id) {
        return new TagInfo(id, "알 수 없음", "gray", "SOLID");
    }
}
