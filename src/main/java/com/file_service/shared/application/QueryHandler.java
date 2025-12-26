package com.file_service.shared.application;

public interface QueryHandler<Q extends Query, R extends Result> {
    R handle(Q query);
}
