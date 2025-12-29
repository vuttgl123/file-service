package com.file_service.files.domain.events;

import com.file_service.shared.domain.AbstractDomainEvent;

public class AssetUploadInitiated extends AbstractDomainEvent {
    private final String assetId;
    private final String assetType;
    private final String storageProvider;
    private final String bucket;
    private final String objectKey;
    private final String uploadUrl;

    public AssetUploadInitiated(
            String assetId,
            String assetType,
            String storageProvider,
            String bucket,
            String objectKey,
            String uploadUrl
    ) {
        super();
        this.assetId = assetId;
        this.assetType = assetType;
        this.storageProvider = storageProvider;
        this.bucket = bucket;
        this.objectKey = objectKey;
        this.uploadUrl = uploadUrl;
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

    public String getStorageProvider() {
        return storageProvider;
    }

    public String getBucket() {
        return bucket;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    @Override
    public String toString() {
        return String.format(
                "AssetUploadInitiated[assetId=%s, type=%s, storage=%s, location=%s/%s]",
                assetId, assetType, storageProvider, bucket, objectKey
        );
    }
}