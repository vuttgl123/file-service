package com.file_service.shared.domain;

@FunctionalInterface
public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T event);
}
