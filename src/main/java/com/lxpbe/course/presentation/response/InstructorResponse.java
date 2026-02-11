package com.lxpbe.course.presentation.response;

public record InstructorResponse(
        Long instructorId,
        String name
) {
    public static InstructorResponse unknown(Long instructorId) {
        return new InstructorResponse(instructorId, "알 수 없음");
    }
}
