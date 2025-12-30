package com.file_service.files.application.assets.query;

import com.file_service.shared.application.Query;

public record GetAssetUrlQuery(
        String assetId
) implements Query {

    public GetAssetUrlQuery {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("Asset ID cannot be null or blank");
        }
    }
}