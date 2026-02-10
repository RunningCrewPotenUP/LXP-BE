package com.recommend.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root 기본 클래스
 * - Domain Event 관리 기능 제공
 * - MSA 시절 공통 라이브러리에서 복사
 */
public abstract class AggregateRoot {

    private final transient List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Domain event must not be null");
        }
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
