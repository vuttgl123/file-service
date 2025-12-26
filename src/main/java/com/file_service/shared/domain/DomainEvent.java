package com.file_service.shared.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public interface DomainEvent extends Serializable {
    String eventId();
    Instant occurredAt();
    String eventType();
    String version();
    String aggregateId();

    default String aggregateType() {
        return this.getClass().getSimpleName().replace("Event", "");
    }
}

