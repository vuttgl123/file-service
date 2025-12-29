package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.StorageProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StorageProviderConverter implements AttributeConverter<StorageProvider, String> {

    @Override
    public String convertToDatabaseColumn(StorageProvider attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public StorageProvider convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return StorageProvider.fromValue(dbData);
    }
}