package com.file_service.files.domain.events;

import com.file_service.shared.domain.AbstractDomainEvent;

public class AssetDeleted extends AbstractDomainEvent {
    private final String assetId;
    private final String assetType;
    private final String bucket;
    private final String objectKey;
    private final String deletedBy;

    public AssetDeleted(
            String assetId,
            String assetType,
            String bucket,
            String objectKey,
            String deletedBy
    ) {
        super();
        this.assetId = assetId;
        this.assetType = assetType;
        this.bucket = bucket;
        this.objectKey = objectKey;
        this.deletedBy = deletedBy;
    }

    public AssetDeleted(
            String assetId,
            String assetType,
            String bucket,
            String objectKey
    ) {
        this(assetId, assetType, bucket, objectKey, null);
    }

    @Override
    public String aggregateId() {
        return assetId;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public String getBucket() {
        return bucket;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    @Override
    public String toString() {
        return String.format(
                "AssetDeleted[assetId=%s, type=%s, location=%s/%s]",
                assetId, assetType, bucket, objectKey
        );
    }
}