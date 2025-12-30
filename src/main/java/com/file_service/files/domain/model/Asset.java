package com.file_service.files.domain.model;

import com.file_service.files.domain.events.AssetDeleted;
import com.file_service.files.domain.events.AssetUploadConfirmed;
import com.file_service.files.domain.events.AssetUploadFailed;
import com.file_service.files.domain.events.AssetUploadInitiated;
import com.file_service.shared.domain.AggregateRoot;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Asset extends AggregateRoot<AssetId> {
    private AssetType type;
    private AssetStatus status;
    private StorageProvider storageProvider;
    private ObjectLocation location;
    private AssetMetadata metadata;

    protected Asset() {
        super();
    }

    private Asset(
            AssetId id,
            AssetType type,
            StorageProvider storageProvider,
            ObjectLocation location
    ) {
        super(id);
        this.type = Objects.requireNonNull(type, "AssetType cannot be null");
        this.status = AssetStatus.PENDING;
        this.storageProvider = Objects.requireNonNull(storageProvider, "StorageProvider cannot be null");
        this.location = Objects.requireNonNull(location, "ObjectLocation cannot be null");
        this.metadata = null;
    }

    public static Asset initiate(
            AssetId id,
            AssetType type,
            StorageProvider storageProvider,
            ObjectLocation location,
            String uploadUrl
    ) {
        Asset asset = new Asset(id, type, storageProvider, location);

        asset.registerEvent(new AssetUploadInitiated(
                id.getValue(),
                type.getValue(),
                storageProvider.getValue(),
                location.getBucket(),
                location.getObjectKey(),
                uploadUrl
        ));

        return asset;
    }

    public void confirm(AssetMetadata metadata) {
        if (metadata == null) {
            throw new IllegalArgumentException("AssetMetadata cannot be null");
        }

        this.status.validateTransition(AssetStatus.CONFIRMED);
        metadata.validateForType(this.type);
        this.metadata = metadata;
        this.status = AssetStatus.CONFIRMED;

        applyStateChange();
        registerEvent(new AssetUploadConfirmed(
                getId().getValue(),
                type.getValue(),
                location.getBucket(),
                location.getObjectKey(),
                metadata.getMimeType(),
                metadata.getSizeBytes(),
                metadata.getWidth(),
                metadata.getHeight(),
                metadata.getDurationSec(),
                metadata.getChecksum()
        ));
    }

    public void markAsFailed(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Failure reason cannot be null or blank");
        }

        this.status.validateTransition(AssetStatus.FAILED);
        this.status = AssetStatus.FAILED;
        applyStateChange();
        registerEvent(new AssetUploadFailed(
                getId().getValue(),
                location.getBucket(),
                location.getObjectKey(),
                reason
        ));
    }

    public void markAsFailed(String reason, String errorCode) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Failure reason cannot be null or blank");
        }

        this.status.validateTransition(AssetStatus.FAILED);
        this.status = AssetStatus.FAILED;

        applyStateChange();
        registerEvent(new AssetUploadFailed(
                getId().getValue(),
                location.getBucket(),
                location.getObjectKey(),
                reason,
                errorCode
        ));
    }


    public void markForDeletion() {
        registerEvent(new AssetDeleted(
                getId().getValue(),
                type.getValue(),
                location.getBucket(),
                location.getObjectKey()
        ));
    }

    public boolean isPending() {
        return status.isPending();
    }

    public boolean isConfirmed() {
        return status.isConfirmed();
    }

    public boolean isFailed() {
        return status.isFailed();
    }

    public boolean isImage() {
        return type == AssetType.IMAGE;
    }

    public boolean isVideo() {
        return type == AssetType.VIDEO;
    }

    public boolean hasMetadata() {
        return metadata != null;
    }

    public String getFullPath() {
        return location.getFullPath();
    }

    public String getFileName() {
        return location.getFileName();
    }

    public String getFileExtension() {
        return location.getFileExtension();
    }

    @Override
    public void validate() {
        super.validate();

        if (type == null) {
            throw new IllegalStateException("AssetType is required");
        }
        if (status == null) {
            throw new IllegalStateException("AssetStatus is required");
        }
        if (storageProvider == null) {
            throw new IllegalStateException("StorageProvider is required");
        }
        if (location == null) {
            throw new IllegalStateException("ObjectLocation is required");
        }

        if (status.isConfirmed() && metadata == null) {
            throw new IllegalStateException("Confirmed asset must have metadata");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Asset[id=%s, type=%s, status=%s, storage=%s, location=%s]",
                getId(), type, status, storageProvider, location
        );
    }
}