package com.file_service.files.domain.ports;

import java.util.Map;

public interface StoragePort {

    PresignedPut presignPut(PresignPutRequest request);

    record PresignPutRequest(
            String bucket,
            String objectKey,
            String contentType
    ) {}

    record PresignedPut(
            String uploadUrl,
            String bucket,
            String objectKey,
            Map<String, String> requiredHeaders
    ) {}
}
