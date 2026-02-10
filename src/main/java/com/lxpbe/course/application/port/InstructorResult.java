package com.lxpbe.course.application.port;

public record InstructorResult(
        Long instructorId,
        String name
) {
    public static InstructorResult unknown(Long instructorId) {
        return new InstructorResult(instructorId, "알 수 없음");
    }
}
