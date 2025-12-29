package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.AssetStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetStatusConverter implements AttributeConverter<AssetStatus, String> {

    @Override
    public String convertToDatabaseColumn(AssetStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public AssetStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return AssetStatus.fromValue(dbData);
    }
}