package com.lxpbe.course.port;

import java.util.List;

public interface TagPort {
    List<TagInfo> findTagsByIds(List<Long> tagIds);
}
