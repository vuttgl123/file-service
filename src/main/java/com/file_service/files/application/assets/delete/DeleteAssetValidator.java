package com.file_service.files.application.assets.delete;

import com.file_service.files.domain.model.Asset;
import com.file_service.shared.application.ValidationException;
import com.file_service.shared.application.Validator;

public class DeleteAssetValidator implements Validator<Asset> {

    @Override
    public void validate(Asset asset) throws ValidationException {
        if (asset == null) {
            throw new ValidationException("Asset cannot be null");
        }
    }
}