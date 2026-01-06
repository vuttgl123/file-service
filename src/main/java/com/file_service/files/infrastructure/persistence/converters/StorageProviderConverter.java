package com.file_service.files.infrastructure.persistence.converters;

import com.file_service.files.domain.model.StorageProvider;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StorageProviderConverter extends GenericStringConverter<StorageProvider> {
    public StorageProviderConverter() {
        super(StorageProvider::getValue, StorageProvider::fromValue);
    }
}