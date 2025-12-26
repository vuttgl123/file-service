package com.file_service.shared.domain;

import java.util.List;

public interface DomainEventHandlerRegistry {
    <T extends DomainEvent> void register(Class<T> eventType, DomainEventHandler<T> handler);
    <T extends DomainEvent> List<DomainEventHandler<T>> getHandlers(Class<T> eventType);
    <T extends DomainEvent> void unregister(Class<T> eventType, DomainEventHandler<T> handler);
}
