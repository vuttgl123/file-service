package com.file_service.files.application.assets.delete;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.domain.ports.StoragePort;
import com.file_service.shared.application.CommandHandler;
import com.file_service.shared.application.TransactionalUseCase;
import com.file_service.shared.application.UseCase;
import com.file_service.shared.application.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TransactionalUseCase
public class DeleteAssetUseCase implements
        UseCase<DeleteAssetCommand, DeleteAssetResult>,
        CommandHandler<DeleteAssetCommand, DeleteAssetResult> {

    private static final Logger log = LoggerFactory.getLogger(DeleteAssetUseCase.class);

    private final AssetRepository assetRepository;
    private final StoragePort storagePort;
    private final DeleteAssetValidator validator = new DeleteAssetValidator();

    public DeleteAssetUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort
    ) {
        this.assetRepository = assetRepository;
        this.storagePort = storagePort;
    }

    @Override
    public DeleteAssetResult execute(DeleteAssetCommand command) {
        return handle(command);
    }

    @Override
    public DeleteAssetResult handle(DeleteAssetCommand command) {
        AssetId assetId = AssetId.of(command.assetId());

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ValidationException(
                        "Asset not found with ID: " + command.assetId()
                ));

        validator.validate(asset);
        asset.markForDeletion();

        String bucket = asset.getLocation().getBucket();
        String objectKey = asset.getLocation().getObjectKey();

        try {
            log.info("Deleting asset from storage: bucket={}, key={}", bucket, objectKey);
            storagePort.deleteObject(bucket, objectKey);

            log.info("Deleting asset from database: id={}", assetId);
            assetRepository.deleteById(assetId);

            log.info("Asset deleted successfully: id={}", assetId);
            return DeleteAssetResult.success(asset.getId().getValue());

        } catch (Exception e) {
            log.error("Failed to delete asset: id={}, error={}", assetId, e.getMessage(), e);
            throw new RuntimeException(
                    "Failed to delete asset: " + e.getMessage(), e
            );
        }
    }
}