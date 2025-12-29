package com.file_service.files.application.assets.query;

import com.file_service.files.domain.model.Asset;
import com.file_service.files.domain.model.AssetStatus;

public class GetAssetUrlValidator {

    public void validate(Asset asset) {
        if (asset.getStatus() != AssetStatus.CONFIRMED) {
            throw new IllegalStateException("Asset is not confirmed");
        }
    }
}
