package com.file_service.files.application.assets.query;

public record GetAssetUrlResult(
        String assetId,
        String publicUrl
) {}
