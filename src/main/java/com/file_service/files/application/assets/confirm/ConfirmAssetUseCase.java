package com.file_service.files.application.assets.confirm;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.model.AssetMetadata;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.shared.application.CommandHandler;
import com.file_service.shared.application.TransactionalUseCase;
import com.file_service.shared.application.UseCase;
import com.file_service.shared.application.ValidationException;

@TransactionalUseCase
public class ConfirmAssetUseCase implements
        UseCase<ConfirmAssetCommand, ConfirmAssetResult>,
        CommandHandler<ConfirmAssetCommand, ConfirmAssetResult> {

    private final AssetRepository assetRepository;
    private final ConfirmAssetValidator validator = new ConfirmAssetValidator();

    public ConfirmAssetUseCase(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public ConfirmAssetResult execute(ConfirmAssetCommand request) {
        return handle(request);
    }

    @Override
    public ConfirmAssetResult handle(ConfirmAssetCommand command) {
        validator.validate(command);

        AssetId assetId = AssetId.of(command.assetId());
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ValidationException(
                        "Asset not found with ID: " + command.assetId()
                ));

        if (!asset.getLocation().getBucket().equals(command.bucket()) ||
                !asset.getLocation().getObjectKey().equals(command.objectKey())) {
            throw new ValidationException(
                    "Bucket/ObjectKey mismatch for asset: " + command.assetId()
            );
        }

        if (!asset.isPending()) {
            throw new ValidationException(
                    "Asset is not in PENDING state. Current status: " + asset.getStatus()
            );
        }

        AssetMetadata metadata = AssetMetadata.builder()
                .mimeType(command.mimeType())
                .sizeBytes(command.sizeBytes())
                .width(command.width())
                .height(command.height())
                .durationSec(command.durationSec())
                .checksum(command.checksum())
                .build();

        try {
            asset.confirm(metadata);
        } catch (IllegalArgumentException e) {
            asset.markAsFailed("Metadata validation failed: " + e.getMessage());
            assetRepository.save(asset);
            throw new ValidationException("Asset confirmation failed: " + e.getMessage(), e);
        }
        assetRepository.save(asset);

        return new ConfirmAssetResult(
                asset.getId().getValue(),
                asset.getStatus().getValue(),
                asset.getLocation().getBucket(),
                asset.getLocation().getObjectKey()
        );
    }
}