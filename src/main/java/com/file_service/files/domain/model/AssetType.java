package com.file_service.files.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum AssetType {
    IMAGE("image", Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    ), 10 * 1024 * 1024L), // 10MB

    VIDEO("video", Set.of(
            "video/mp4",
            "video/webm",
            "video/quicktime"
    ), 100 * 1024 * 1024L); // 100MB

    private final String value;
    private final Set<String> allowedMimeTypes;
    private final long maxSizeBytes;

    AssetType(String value, Set<String> allowedMimeTypes, long maxSizeBytes) {
        this.value = value;
        this.allowedMimeTypes = allowedMimeTypes;
        this.maxSizeBytes = maxSizeBytes;
    }

    public boolean isMimeTypeAllowed(String mimeType) {
        if (mimeType == null || mimeType.isBlank()) {
            return false;
        }
        return allowedMimeTypes.contains(mimeType.toLowerCase());
    }

    public boolean isSizeValid(Long sizeBytes) {
        if (sizeBytes == null || sizeBytes <= 0) {
            return false;
        }
        return sizeBytes <= maxSizeBytes;
    }

    public static AssetType fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AssetType value cannot be null or blank");
        }
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid AssetType: " + value + ". Allowed values: " +
                                Arrays.stream(values())
                                        .map(AssetType::getValue)
                                        .collect(Collectors.joining(", "))
                ));
    }

    public static AssetType fromMimeType(String mimeType) {
        if (mimeType == null || mimeType.isBlank()) {
            throw new IllegalArgumentException("MimeType cannot be null or blank");
        }
        return Arrays.stream(values())
                .filter(type -> type.isMimeTypeAllowed(mimeType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported mime type: " + mimeType
                ));
    }
}