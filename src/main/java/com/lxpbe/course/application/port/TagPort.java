package com.lxpbe.course.application.port;

import java.util.List;

public interface TagPort {
    List<TagResult> findTagsByIds(List<Long> tagIds);
}
