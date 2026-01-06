package com.file_service.files.infrastructure.persistence.converters;

import jakarta.persistence.AttributeConverter;

import java.util.function.Function;

public class GenericStringConverter<T> implements AttributeConverter<T, String> {

    private final Function<T, String> toDatabase;
    private final Function<String, T> fromDatabase;

    public GenericStringConverter(Function<T, String> toDatabase, Function<String, T> fromDatabase) {
        this.toDatabase = toDatabase;
        this.fromDatabase = fromDatabase;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : toDatabase.apply(attribute);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : fromDatabase.apply(dbData);
    }
}