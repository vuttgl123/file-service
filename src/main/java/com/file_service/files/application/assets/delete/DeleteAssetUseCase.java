package com.file_service.files.application.assets.delete;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.domain.ports.StoragePort;

public class DeleteAssetUseCase {

    private final AssetRepository assetRepository;
    private final StoragePort storagePort;
    private final DeleteAssetValidator validator;

    public DeleteAssetUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort,
            DeleteAssetValidator validator
    ) {
        this.assetRepository = assetRepository;
        this.storagePort = storagePort;
        this.validator = validator;
    }

    public void handle(DeleteAssetCommand command) {
        Asset asset = assetRepository.findById(new AssetId(command.assetId()))
                .orElseThrow(() -> new IllegalStateException("Asset not found"));


        validator.validate(asset);

        storagePort.deleteObject(
                asset.getLocation().getBucket(),
                asset.getLocation().getObjectKey()
        );

        assetRepository.deleteById(asset.getId());
    }
}
