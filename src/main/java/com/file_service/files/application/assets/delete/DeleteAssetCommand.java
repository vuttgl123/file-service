package com.file_service.files.application.assets.delete;

import com.file_service.shared.application.Command;
import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteAssetCommand(
        @Schema(description = "Asset ID from presign response", example = "550e8400-e29b-41d4-a716-446655440000")
        String assetId
) implements Command {

    public DeleteAssetCommand {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("Asset ID cannot be null or blank");
        }
    }
}