package com.lxpbe.course.infra;

import com.lxpbe.course.port.InstructorInfo;
import com.lxpbe.course.port.UserPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * User BC가 구현되기 전까지 사용되는 임시 어댑터.
 * 실제 User BC가 구현되면 해당 BC에서 UserPort 구현체를 제공해야 함.
 */
@Component
public class StubUserAdapter implements UserPort {

    @Override
    public Optional<InstructorInfo> findInstructorById(Long instructorId) {
        // TODO: User BC 구현 후 실제 조회 로직으로 교체
        return Optional.of(new InstructorInfo(instructorId, "테스트 강사"));
    }
}
