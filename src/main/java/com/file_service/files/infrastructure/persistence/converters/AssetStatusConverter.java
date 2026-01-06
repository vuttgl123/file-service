package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.AssetStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetStatusConverter extends GenericStringConverter<AssetStatus> {
    public AssetStatusConverter() {
        super(AssetStatus::getValue, AssetStatus::fromValue);
    }
}