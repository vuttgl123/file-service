package com.file_service.shared.domain;

public interface TypedIdGenerator<T> {
    T generate();
    T from(String value);
}
