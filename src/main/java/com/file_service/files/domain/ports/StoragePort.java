package com.file_service.files.domain.ports;

import java.util.Map;

public interface StoragePort {

    PresignedPut presignPut(PresignPutRequest request);

    String resolvePublicUrl(String bucket, String objectKey);

    boolean objectExists(String bucket, String objectKey);

    void deleteObject(String bucket, String objectKey);

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