package com.lxpbe.course.infrastructure;

import com.lxpbe.course.application.port.TagResult;
import com.lxpbe.course.application.port.TagPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Tag BC가 구현되기 전까지 사용되는 임시 어댑터.
 * 실제 Tag BC가 구현되면 해당 BC에서 TagPort 구현체를 제공해야 함.
 */
@Component
public class StubTagAdapter implements TagPort {

    private static final Map<Long, TagResult> STUB_TAGS = Map.of(
            1L, new TagResult(1L, "Spring"),
            2L, new TagResult(2L, "DDD"),
            3L, new TagResult(3L, "Redis"),
            4L, new TagResult(4L, "Java")
    );

    @Override
    public List<TagResult> findTagsByIds(List<Long> tagIds) {
        // TODO: Tag BC 구현 후 실제 조회 로직으로 교체
        return tagIds.stream()
                .map(id -> STUB_TAGS.getOrDefault(id, TagResult.unknown(id)))
                .toList();
    }
}
