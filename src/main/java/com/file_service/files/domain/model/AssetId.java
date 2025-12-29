package com.file_service.files.domain.model;

import com.file_service.shared.domain.AbstractValueObject;

import java.util.Objects;
import java.util.UUID;

public final class AssetId extends AbstractValueObject {
    private final String value;

    public AssetId(String value) {
        this.value = value;
        validate();
    }

    public static AssetId of(String value) {
        return new AssetId(value);
    }

    public static AssetId generate() {
        return new AssetId(UUID.randomUUID().toString());
    }

    public static AssetId fromUUID(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        return new AssetId(uuid.toString());
    }

    public String getValue() {
        return value;
    }

    public UUID toUUID() {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("AssetId value is not a valid UUID: " + value, e);
        }
    }

    @Override
    public void validate() {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AssetId value cannot be null or blank");
        }

        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("AssetId must be a valid UUID format: " + value, e);
        }
    }

    @Override
    protected boolean equalsByValue(AbstractValueObject other) {
        if (!(other instanceof AssetId that)) {
            return false;
        }
        return Objects.equals(this.value, that.value);
    }

    @Override
    protected int hashCodeByValue() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}