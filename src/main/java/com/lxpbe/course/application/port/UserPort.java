package com.lxpbe.course.application.port;

import java.util.Optional;

public interface UserPort {
    Optional<InstructorResult> findInstructorById(Long instructorId);
}
