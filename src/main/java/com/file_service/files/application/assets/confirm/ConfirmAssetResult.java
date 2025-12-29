package com.file_service.files.application.assets.confirm;

import com.file_service.shared.application.Result;

public record ConfirmAssetResult(
        String assetId,
        String status,
        String bucket,
        String objectKey
) implements Result {
}