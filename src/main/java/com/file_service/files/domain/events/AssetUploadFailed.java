package com.file_service.files.domain.events;

import com.file_service.shared.domain.AbstractDomainEvent;

public class AssetUploadFailed extends AbstractDomainEvent {
    private final String assetId;
    private final String bucket;
    private final String objectKey;
    private final String reason;
    private final String errorCode;

    public AssetUploadFailed(
            String assetId,
            String bucket,
            String objectKey,
            String reason,
            String errorCode
    ) {
        super();
        this.assetId = assetId;
        this.bucket = bucket;
        this.objectKey = objectKey;
        this.reason = reason;
        this.errorCode = errorCode;
    }

    public AssetUploadFailed(
            String assetId,
            String bucket,
            String objectKey,
            String reason
    ) {
        this(assetId, bucket, objectKey, reason, null);
    }

    @Override
    public String aggregateId() {
        return assetId;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getBucket() {
        return bucket;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getReason() {
        return reason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.isBlank();
    }

    @Override
    public String toString() {
        String base = String.format(
                "AssetUploadFailed[assetId=%s, location=%s/%s, reason=%s",
                assetId, bucket, objectKey, reason
        );
        if (hasErrorCode()) {
            base += ", errorCode=" + errorCode;
        }
        return base + "]";
    }
}