package com.lxpbe.course.port;

public record InstructorInfo(
        Long instructorId,
        String name
) {
    public static InstructorInfo unknown(Long instructorId) {
        return new InstructorInfo(instructorId, "알 수 없음");
    }
}
