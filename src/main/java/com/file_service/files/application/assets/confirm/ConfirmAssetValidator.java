package com.file_service.files.application.assets.confirm;

import com.file_service.shared.application.ValidationException;
import com.file_service.shared.application.Validator;

public class ConfirmAssetValidator implements Validator<ConfirmAssetCommand> {

    @Override
    public void validate(ConfirmAssetCommand command) throws ValidationException {
        if (command == null) {
            throw new ValidationException("Command cannot be null");
        }

        if (command.assetId() == null || command.assetId().isBlank()) {
            throw new ValidationException("Asset ID is required");
        }

        if (command.bucket() == null || command.bucket().isBlank()) {
            throw new ValidationException("Bucket is required");
        }

        if (command.objectKey() == null || command.objectKey().isBlank()) {
            throw new ValidationException("Object key is required");
        }

        if (command.mimeType() == null || command.mimeType().isBlank()) {
            throw new ValidationException("MIME type is required");
        }

        if (command.sizeBytes() == null || command.sizeBytes() <= 0) {
            throw new ValidationException("Size must be greater than 0");
        }

        if (command.width() != null && command.width() <= 0) {
            throw new ValidationException("Width must be greater than 0");
        }
        if (command.height() != null && command.height() <= 0) {
            throw new ValidationException("Height must be greater than 0");
        }

        if (command.durationSec() != null && command.durationSec() <= 0) {
            throw new ValidationException("Duration must be greater than 0");
        }
    }
}