package com.file_service.files.application.assets.delete;

import com.file_service.shared.application.Command;

public record DeleteAssetCommand(
        String assetId
) implements Command {

    public DeleteAssetCommand {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("Asset ID cannot be null or blank");
        }
    }
}