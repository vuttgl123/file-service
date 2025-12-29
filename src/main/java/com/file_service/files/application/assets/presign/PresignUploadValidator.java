package com.file_service.files.application.assets.presign;

import com.file_service.files.domain.model.AssetType;
import com.file_service.shared.application.ValidationException;
import com.file_service.shared.application.Validator;

public class PresignUploadValidator implements Validator<PresignUploadCommand> {

    @Override
    public void validate(PresignUploadCommand command) throws ValidationException {
        if (command == null) {
            throw new ValidationException("Command cannot be null");
        }

        if (command.type() == null || command.type().isBlank()) {
            throw new ValidationException("Asset type is required");
        }

        try {
            AssetType assetType = AssetType.fromValue(command.type());
            if (command.contentType() == null || command.contentType().isBlank()) {
                throw new ValidationException("Content type is required");
            }
            if (!assetType.isMimeTypeAllowed(command.contentType())) {
                throw new ValidationException(
                        String.format("Content type '%s' is not allowed for asset type '%s'. Allowed types: %s",
                                command.contentType(),
                                command.type(),
                                assetType.getAllowedMimeTypes())
                );
            }

        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid asset type: " + command.type(), e);
        }

        if (command.fileName() != null && command.fileName().length() > 255) {
            throw new ValidationException("File name is too long (max 255 characters)");
        }
    }
}