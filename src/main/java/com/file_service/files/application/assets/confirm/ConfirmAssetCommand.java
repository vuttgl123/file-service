package com.file_service.files.application.assets.confirm;

import com.file_service.shared.application.Command;

public record ConfirmAssetCommand(
        String assetId,
        String bucket,
        String objectKey,
        String mimeType,
        Long sizeBytes,
        Integer width,
        Integer height,
        Double durationSec,
        String checksum
) implements Command {
}