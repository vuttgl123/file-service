package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.AssetType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetTypeConverter extends GenericStringConverter<AssetType> {
    public AssetTypeConverter() {
        super(AssetType::getValue, AssetType::fromValue);
    }
}