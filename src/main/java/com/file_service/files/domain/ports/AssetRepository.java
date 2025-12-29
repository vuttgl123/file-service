package com.file_service.files.domain.ports;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.model.AssetStatus;

import java.util.Optional;

public interface AssetRepository {

    Asset save(Asset asset);

    Optional<Asset> findById(AssetId id);

    boolean existsByBucketAndObjectKey(String bucket, String objectKey);

    Optional<Asset> findByBucketAndObjectKey(String bucket, String objectKey);

    void deleteById(AssetId id);

    long countByStatus(AssetStatus status);

    int deletePendingAssetsOlderThan(int minutesOld);
}