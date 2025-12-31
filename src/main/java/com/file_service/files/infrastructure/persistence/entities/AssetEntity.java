package com.file_service.files.infrastructure.persistence.entities;

import com.file_service.files.domain.model.*;
import com.file_service.files.infrastructure.persistence.converters.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "assets",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_assets_bucket_object", columnNames = {"bucket", "object_key"})
        },
        indexes = {
                @Index(name = "idx_assets_type", columnList = "type"),
                @Index(name = "idx_assets_status", columnList = "status"),
                @Index(name = "idx_assets_status_created", columnList = "status, created_at")
        }
)
public class AssetEntity {

    @Getter
    @Column(name = "id", nullable = false, updatable = false)
    @EmbeddedId
    private AssetId id;

    @Getter
    @Column(name = "type", nullable = false)
    @Convert(converter = AssetTypeConverter.class)
    private AssetType type;

    @Getter
    @Column(name = "status", nullable = false)
    @Convert(converter = AssetStatusConverter.class)
    private AssetStatus status;

    @Column(name = "storage", nullable = false)
    @Convert(converter = StorageProviderConverter.class)
    private StorageProvider storage;

    @Getter
    @Column(name = "bucket", nullable = false)
    private String bucket;

    @Getter
    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "duration_sec")
    private Double durationSec;

    @Column(name = "checksum")
    private String checksum;

    @Getter
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    protected AssetEntity() {
    }

    public static AssetEntity fromDomain(Asset asset) {
        AssetEntity entity = new AssetEntity();
        entity.id = asset.getId();
        entity.type = asset.getType();
        entity.status = asset.getStatus();
        entity.storage = asset.getStorageProvider();
        entity.bucket = asset.getLocation().getBucket();
        entity.objectKey = asset.getLocation().getObjectKey();

        if (asset.hasMetadata()) {
            AssetMetadata metadata = asset.getMetadata();
            entity.mimeType = metadata.getMimeType();
            entity.sizeBytes = metadata.getSizeBytes();
            entity.width = metadata.getWidth();
            entity.height = metadata.getHeight();
            entity.durationSec = metadata.getDurationSec();
            entity.checksum = metadata.getChecksum();
        }

        entity.createdAt = asset.getCreatedAt();
        entity.updatedAt = asset.getUpdatedAt();
        entity.version = asset.getVersion();

        return entity;
    }

    public Asset toDomain() {
        ObjectLocation location = ObjectLocation.of(bucket, objectKey);
        Asset asset = AssetMapper.reconstruct(
                id,
                type,
                status,
                storage,
                location,
                buildMetadata(),
                createdAt,
                updatedAt,
                version
        );

        return asset;
    }

    private AssetMetadata buildMetadata() {
        if (mimeType == null) {
            return null;
        }

        return AssetMetadata.builder()
                .mimeType(mimeType)
                .sizeBytes(sizeBytes)
                .width(width)
                .height(height)
                .durationSec(durationSec)
                .checksum(checksum)
                .build();
    }
}