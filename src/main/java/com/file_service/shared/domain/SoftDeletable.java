package com.file_service.shared.domain;

public interface SoftDeletable {
    void markAsDeleted();
    boolean isDeleted();
    void restore();
}
