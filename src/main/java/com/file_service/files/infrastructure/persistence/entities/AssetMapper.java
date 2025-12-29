package com.file_service.files.infrastructure.persistence.entities;

import com.file_service.files.domain.model.*;

import java.lang.reflect.Field;
import java.time.Instant;

public class AssetMapper {
    public static Asset reconstruct(
            AssetId id,
            AssetType type,
            AssetStatus status,
            StorageProvider storageProvider,
            ObjectLocation location,
            AssetMetadata metadata,
            Instant createdAt,
            Instant updatedAt,
            Long version
    ) {
        try {
            Asset asset = (Asset) getUnsafe().allocateInstance(Asset.class);
            setField(asset, "id", id, com.file_service.shared.domain.Entity.class);
            setField(asset, "createdAt", createdAt, com.file_service.shared.domain.Entity.class);
            setField(asset, "updatedAt", updatedAt, com.file_service.shared.domain.Entity.class);
            setField(asset, "version", version, com.file_service.shared.domain.AggregateRoot.class);
            setField(asset, "type", type, Asset.class);
            setField(asset, "status", status, Asset.class);
            setField(asset, "storageProvider", storageProvider, Asset.class);
            setField(asset, "location", location, Asset.class);
            setField(asset, "metadata", metadata, Asset.class);
            return asset;
        } catch (Exception e) {
            throw new RuntimeException("Failed to reconstruct Asset from persistence", e);
        }
    }

    private static void setField(Object target, String fieldName, Object value, Class<?> declaringClass)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = declaringClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static sun.misc.Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (sun.misc.Unsafe) f.get(null);
    }
}