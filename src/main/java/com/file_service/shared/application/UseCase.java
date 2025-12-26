package com.file_service.shared.application;

public interface UseCase<REQUEST, RESPONSE> {
    RESPONSE execute(REQUEST request);
}
