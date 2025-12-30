package com.file_service.files.domain.model;

import com.file_service.shared.domain.AbstractValueObject;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class ObjectLocation extends AbstractValueObject {
    private final String bucket;
    private final String objectKey;

    private ObjectLocation(String bucket, String objectKey) {
        this.bucket = bucket;
        this.objectKey = objectKey;
        validate();
    }

    public static ObjectLocation of(String bucket, String objectKey) {
        return new ObjectLocation(bucket, objectKey);
    }

    public String getFullPath() {
        return bucket + "/" + objectKey;
    }

    public String getFileExtension() {
        if (objectKey == null || !objectKey.contains(".")) {
            return "";
        }
        return objectKey.substring(objectKey.lastIndexOf(".") + 1).toLowerCase();
    }

    public String getFileName() {
        if (objectKey == null) {
            return "";
        }
        int lastSlash = objectKey.lastIndexOf("/");
        return lastSlash >= 0 ? objectKey.substring(lastSlash + 1) : objectKey;
    }

    @Override
    public void validate() {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("Bucket cannot be null or blank");
        }
        if (objectKey == null || objectKey.isBlank()) {
            throw new IllegalArgumentException("Object key cannot be null or blank");
        }

        if (bucket.length() < 3 || bucket.length() > 63) {
            throw new IllegalArgumentException("Bucket name must be between 3 and 63 characters");
        }
        if (!bucket.matches("^[a-z0-9][a-z0-9.-]*[a-z0-9]$")) {
            throw new IllegalArgumentException("Bucket name contains invalid characters");
        }

        if (objectKey.length() > 1024) {
            throw new IllegalArgumentException("Object key cannot exceed 1024 characters");
        }
    }

    @Override
    protected boolean equalsByValue(AbstractValueObject other) {
        if (!(other instanceof ObjectLocation that)) {
            return false;
        }
        return Objects.equals(this.bucket, that.bucket) &&
                Objects.equals(this.objectKey, that.objectKey);
    }

    @Override
    protected int hashCodeByValue() {
        return Objects.hash(bucket, objectKey);
    }

    @Override
    public String toString() {
        return getFullPath();
    }
}