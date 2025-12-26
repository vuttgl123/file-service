package com.file_service.shared.application;

public interface Validator<T> {
    void validate(T target);
}
