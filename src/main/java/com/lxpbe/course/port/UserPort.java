package com.lxpbe.course.port;

import java.util.Optional;

public interface UserPort {
    Optional<InstructorInfo> findInstructorById(Long instructorId);
}
