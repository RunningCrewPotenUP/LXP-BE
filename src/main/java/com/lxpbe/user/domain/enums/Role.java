package com.lxpbe.user.domain.enums;

import lombok.Getter;

public enum Role {
    LEARNER("학습자"),
    INSTRUCTOR("강사"),
    ADMIN("관리자"),
    CS_MANAGER("CS 담당자")
    ;

    @Getter
    private final String description;

    Role(String description) {
        this.description = description;
    }
}
