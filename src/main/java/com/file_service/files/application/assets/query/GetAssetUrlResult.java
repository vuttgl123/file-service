package com.file_service.files.application.assets.query;

import com.file_service.shared.application.Result;

public record GetAssetUrlResult(
        String assetId,
        String publicUrl,
        String bucket,
        String objectKey,
        String type,
        String mimeType,
        Long sizeBytes
) implements Result {
}
