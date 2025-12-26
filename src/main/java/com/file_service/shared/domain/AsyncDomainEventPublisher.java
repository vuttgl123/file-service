package com.file_service.shared.domain;

import java.util.List;

public interface AsyncDomainEventPublisher extends DomainEventPublisher {
    void publishAsync(DomainEvent event);
    void publishAllAsync(List<DomainEvent> events);
}
