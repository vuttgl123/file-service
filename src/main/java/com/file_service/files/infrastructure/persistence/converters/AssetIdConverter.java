package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.AssetId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetIdConverter implements AttributeConverter<AssetId, String> {

    @Override
    public String convertToDatabaseColumn(AssetId attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public AssetId convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return AssetId.of(dbData);
    }
}