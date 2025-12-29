package com.file_service.files.infrastructure.storage;

import com.file_service.files.domain.ports.StoragePort;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;

public class R2StorageAdapter implements StoragePort {

    private final S3Presigner presigner;
    private final Duration expiry;

    public R2StorageAdapter(S3Presigner presigner, Duration expiry) {
        this.presigner = presigner;
        this.expiry = expiry;
    }

    @Override
    public PresignedPut presignPut(PresignPutRequest request) {
        var putObject = PutObjectRequest.builder()
                .bucket(request.bucket())
                .key(request.objectKey())
                .contentType(request.contentType())
                .build();

        var presignReq = PutObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .putObjectRequest(putObject)
                .build();

        var presigned = presigner.presignPutObject(presignReq);

        return new PresignedPut(
                presigned.url().toString(),
                request.bucket(),
                request.objectKey(),
                Map.of("Content-Type", request.contentType())
        );
    }
}
