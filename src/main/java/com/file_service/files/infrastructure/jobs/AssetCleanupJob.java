package com.file_service.files.infrastructure.jobs;

import com.file_service.files.domain.ports.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AssetCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(AssetCleanupJob.class);

    private final AssetRepository assetRepository;
    private final int cleanupThresholdMinutes;

    public AssetCleanupJob(
            AssetRepository assetRepository,
            @Value("${app.cleanup.pending-assets-threshold-minutes:60}") int cleanupThresholdMinutes
    ) {
        this.assetRepository = assetRepository;
        this.cleanupThresholdMinutes = cleanupThresholdMinutes;
    }

    @Scheduled(cron = "${app.cleanup.cron:0 0 * * * *}")
    public void cleanupPendingAssets() {
        log.info("Starting cleanup of pending assets older than {} minutes", cleanupThresholdMinutes);

        try {
            int deletedCount = assetRepository.deletePendingAssetsOlderThan(cleanupThresholdMinutes);

            if (deletedCount > 0) {
                log.info("Cleaned up {} pending assets", deletedCount);
            } else {
                log.debug("No pending assets to cleanup");
            }

        } catch (Exception e) {
            log.error("Failed to cleanup pending assets: {}", e.getMessage(), e);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void logCleanupConfiguration() {
        log.info("Asset cleanup job configured: threshold={}min, cron=${app.cleanup.cron:0 0 * * * *}",
                cleanupThresholdMinutes);
    }
}