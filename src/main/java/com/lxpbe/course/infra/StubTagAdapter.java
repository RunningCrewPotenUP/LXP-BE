package com.lxpbe.course.infra;

import com.lxpbe.course.port.TagInfo;
import com.lxpbe.course.port.TagPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Tag BC가 구현되기 전까지 사용되는 임시 어댑터.
 * 실제 Tag BC가 구현되면 해당 BC에서 TagPort 구현체를 제공해야 함.
 */
@Component
public class StubTagAdapter implements TagPort {

    private static final Map<Long, TagInfo> STUB_TAGS = Map.of(
            1L, new TagInfo(1L, "Spring", "green", "SOLID"),
            2L, new TagInfo(2L, "DDD", "blue", "SOLID"),
            3L, new TagInfo(3L, "Redis", "red", "SOLID"),
            4L, new TagInfo(4L, "Java", "orange", "GHOST")
    );

    @Override
    public List<TagInfo> findTagsByIds(List<Long> tagIds) {
        // TODO: Tag BC 구현 후 실제 조회 로직으로 교체
        return tagIds.stream()
                .map(id -> STUB_TAGS.getOrDefault(id, TagInfo.unknown(id)))
                .toList();
    }
}
