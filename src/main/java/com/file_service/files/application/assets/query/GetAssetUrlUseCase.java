package com.file_service.files.application.assets.query;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.domain.ports.StoragePort;

public class GetAssetUrlUseCase {

    private final AssetRepository assetRepository;
    private final StoragePort storagePort;
    private final GetAssetUrlValidator validator;

    public GetAssetUrlUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort,
            GetAssetUrlValidator validator
    ) {
        this.assetRepository = assetRepository;
        this.storagePort = storagePort;
        this.validator = validator;
    }

    public GetAssetUrlResult handle(GetAssetUrlQueryCommand query) {
        Asset asset = assetRepository.findById(new AssetId(query.assetId()))
                .orElseThrow(() -> new IllegalStateException("Asset not found"));

        validator.validate(asset);

        String publicUrl = storagePort.resolvePublicUrl(
                asset.getLocation().getBucket(),
                asset.getLocation().getObjectKey()
        );

        return new GetAssetUrlResult(
                asset.getId().getValue(),
                publicUrl
        );
    }
}
