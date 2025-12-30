package com.file_service.files.application.assets.delete;

import com.file_service.shared.application.Result;

public record DeleteAssetResult(
        String assetId,
        boolean deleted
) implements Result {

    public static DeleteAssetResult success(String assetId) {
        return new DeleteAssetResult(assetId, true);
    }
}