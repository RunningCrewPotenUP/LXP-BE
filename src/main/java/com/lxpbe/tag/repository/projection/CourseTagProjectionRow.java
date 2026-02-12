package com.lxpbe.tag.repository.projection;

public interface CourseTagProjectionRow {
    Long getCourseId();
    Integer getTagOrder();
    Long getTagId();
    String getTagName();
    String getTagCategory();
    String getTagSubCategory();
}
