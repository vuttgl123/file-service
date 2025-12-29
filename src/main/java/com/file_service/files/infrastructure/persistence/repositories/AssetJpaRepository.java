package com.file_service.files.infrastructure.persistence.repositories;

import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.model.AssetStatus;
import com.file_service.files.infrastructure.persistence.entities.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface AssetJpaRepository extends JpaRepository<AssetEntity, AssetId> {
    boolean existsByBucketAndObjectKey(String bucket, String objectKey);

    Optional<AssetEntity> findByBucketAndObjectKey(String bucket, String objectKey);

    long countByStatus(AssetStatus status);

    @Modifying
    @Query("DELETE FROM AssetEntity a WHERE a.status = :status AND a.createdAt < :cutoffTime")
    int deletePendingAssetsOlderThan(
            @Param("status") AssetStatus status,
            @Param("cutoffTime") Instant cutoffTime
    );
}
