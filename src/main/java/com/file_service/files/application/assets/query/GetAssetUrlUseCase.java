package com.file_service.files.application.assets.query;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetId;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.domain.ports.StoragePort;
import com.file_service.shared.application.QueryHandler;
import com.file_service.shared.application.TransactionalUseCase;
import com.file_service.shared.application.UseCase;
import com.file_service.shared.application.ValidationException;

@TransactionalUseCase(readOnly = true)
public class GetAssetUrlUseCase implements
        UseCase<GetAssetUrlQuery, GetAssetUrlResult>,
        QueryHandler<GetAssetUrlQuery, GetAssetUrlResult> {

    private final AssetRepository assetRepository;
    private final StoragePort storagePort;
    private final GetAssetUrlValidator validator = new GetAssetUrlValidator();

    public GetAssetUrlUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort
    ) {
        this.assetRepository = assetRepository;
        this.storagePort = storagePort;
    }

    @Override
    public GetAssetUrlResult execute(GetAssetUrlQuery query) {
        return handle(query);
    }

    @Override
    public GetAssetUrlResult handle(GetAssetUrlQuery query) {
        AssetId assetId = AssetId.of(query.assetId());
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ValidationException(
                        "Asset not found with ID: " + query.assetId()
                ));

        validator.validate(asset);

        String publicUrl = storagePort.resolvePublicUrl(
                asset.getLocation().getBucket(),
                asset.getLocation().getObjectKey()
        );

        return new GetAssetUrlResult(
                asset.getId().getValue(),
                publicUrl,
                asset.getLocation().getBucket(),
                asset.getLocation().getObjectKey(),
                asset.getType().getValue(),
                asset.getMetadata().getMimeType(),
                asset.getMetadata().getSizeBytes()
        );
    }
}