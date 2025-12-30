package com.file_service.files.application.assets.query;

import com.file_service.files.domain.model.Asset;
import com.file_service.shared.application.ValidationException;
import com.file_service.shared.application.Validator;

public class GetAssetUrlValidator implements Validator<Asset> {

    @Override
    public void validate(Asset asset) throws ValidationException {
        if (asset == null) {
            throw new ValidationException("Asset cannot be null");
        }

        if (!asset.isConfirmed()) {
            throw new ValidationException(
                    String.format("Asset is not confirmed. Current status: %s",
                            asset.getStatus().getValue())
            );
        }

        if (!asset.hasMetadata()) {
            throw new ValidationException("Asset has no metadata");
        }
    }
}