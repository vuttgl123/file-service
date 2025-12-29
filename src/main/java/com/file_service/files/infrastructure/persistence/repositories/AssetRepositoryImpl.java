package com.file_service.files.infrastructure.persistence.repositories;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.model.AssetStatus;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.infrastructure.persistence.entities.AssetEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
@Transactional
public class AssetRepositoryImpl implements AssetRepository {

    private final AssetJpaRepository jpaRepository;

    public AssetRepositoryImpl(AssetJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Asset save(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset cannot be null");
        }
        asset.validate();

        AssetEntity entity = AssetEntity.fromDomain(asset);
        AssetEntity savedEntity = jpaRepository.save(entity);

        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asset> findById(AssetId id) {
        if (id == null) {
            throw new IllegalArgumentException("AssetId cannot be null");
        }

        return jpaRepository.findById(id)
                .map(AssetEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByBucketAndObjectKey(String bucket, String objectKey) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("Bucket cannot be null or blank");
        }
        if (objectKey == null || objectKey.isBlank()) {
            throw new IllegalArgumentException("Object key cannot be null or blank");
        }

        return jpaRepository.existsByBucketAndObjectKey(bucket, objectKey);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asset> findByBucketAndObjectKey(String bucket, String objectKey) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("Bucket cannot be null or blank");
        }
        if (objectKey == null || objectKey.isBlank()) {
            throw new IllegalArgumentException("Object key cannot be null or blank");
        }

        return jpaRepository.findByBucketAndObjectKey(bucket, objectKey)
                .map(AssetEntity::toDomain);
    }

    @Override
    public void deleteById(AssetId id) {
        if (id == null) {
            throw new IllegalArgumentException("AssetId cannot be null");
        }

        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AssetStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("AssetStatus cannot be null");
        }

        return jpaRepository.countByStatus(status);
    }

    @Override
    public int deletePendingAssetsOlderThan(int minutesOld) {
        if (minutesOld <= 0) {
            throw new IllegalArgumentException("Minutes must be greater than 0");
        }

        Instant cutoffTime = Instant.now().minusSeconds(minutesOld * 60L);
        return jpaRepository.deletePendingAssetsOlderThan(AssetStatus.PENDING, cutoffTime);
    }
}