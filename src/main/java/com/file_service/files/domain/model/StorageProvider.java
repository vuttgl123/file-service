package com.file_service.files.domain.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum StorageProvider {
    R2("r2", "Cloudflare R2"),
    MINIO("minio", "MinIO"),
    S3("s3", "Amazon S3");

    private final String value;
    private final String displayName;

    StorageProvider(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isR2() {
        return this == R2;
    }

    public boolean isMinio() {
        return this == MINIO;
    }

    public boolean isS3() {
        return this == S3;
    }

    public static StorageProvider fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("StorageProvider value cannot be null or blank");
        }
        return Arrays.stream(values())
                .filter(provider -> provider.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid StorageProvider: " + value + ". Allowed values: " +
                                Arrays.stream(values())
                                        .map(StorageProvider::getValue)
                                        .collect(Collectors.joining(", "))
                ));
    }

    public static StorageProvider getDefault() {
        return R2;
    }
}