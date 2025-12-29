package com.file_service.files.application.assets.presign;

import com.file_service.shared.application.Result;

import java.util.Map;

public record PresignUploadResult(
        String assetId,
        String uploadUrl,
        String bucket,
        String objectKey,
        Map<String, String> requiredHeaders
) implements Result {
}