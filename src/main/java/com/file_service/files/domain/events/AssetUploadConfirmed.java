package com.file_service.files.domain.events;

import com.file_service.shared.domain.AbstractDomainEvent;
import lombok.Getter;

@Getter
public class AssetUploadConfirmed extends AbstractDomainEvent {
    private final String assetId;
    private final String assetType;
    private final String bucket;
    private final String objectKey;
    private final String mimeType;
    private final Long sizeBytes;
    private final Integer width;
    private final Integer height;
    private final Double durationSec;
    private final String checksum;

    public AssetUploadConfirmed(
            String assetId,
            String assetType,
            String bucket,
            String objectKey,
            String mimeType,
            Long sizeBytes,
            Integer width,
            Integer height,
            Double durationSec,
            String checksum
    ) {
        super();
        this.assetId = assetId;
        this.assetType = assetType;
        this.bucket = bucket;
        this.objectKey = objectKey;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
        this.width = width;
        this.height = height;
        this.durationSec = durationSec;
        this.checksum = checksum;
    }

    @Override
    public String aggregateId() {
        return assetId;
    }

    @Override
    public String toString() {
        return String.format(
                "AssetUploadConfirmed[assetId=%s, type=%s, location=%s/%s, size=%d bytes]",
                assetId, assetType, bucket, objectKey, sizeBytes
        );
    }
}