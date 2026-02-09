package com.lxpbe.user.domain;

import lombok.Getter;

public enum Role {
    LEARNER("학습자"),
    INSTRUCTOR("강사"),
    ADMIN("관리자")
    ;

    @Getter
    private final String description;

    Role(String description) {
        this.description = description;
    }
}

