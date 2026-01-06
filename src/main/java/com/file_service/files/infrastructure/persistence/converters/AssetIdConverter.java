package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.AssetId;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetIdConverter extends GenericStringConverter<AssetId> {
    public AssetIdConverter() {
        super(AssetId::getValue, AssetId::of);
    }
}