package com.file_service.files.infrastructure.storage;

import com.file_service.files.domain.ports.StoragePort;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;

public class R2StorageAdapter implements StoragePort {

    private final S3Presigner presigner;
    private final S3Client s3Client;
    private final Duration expiry;
    private final String publicBaseUrl;


    public R2StorageAdapter(S3Presigner presigner, S3Client s3Client, Duration expiry, String publicBaseUrl) {
        this.presigner = presigner;
        this.s3Client = s3Client;
        this.expiry = expiry;
        this.publicBaseUrl = publicBaseUrl;
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

    @Override
    public String resolvePublicUrl(String bucket, String objectKey) {
        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            throw new IllegalStateException("Missing config: app.r2.publicBaseUrl");
        }
        String base = publicBaseUrl.endsWith("/") ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1) : publicBaseUrl;
        String key = objectKey.startsWith("/") ? objectKey.substring(1) : objectKey;
        return base + "/" + key;
    }

    @Override
    public void deleteObject(String bucket, String objectKey) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build());
    }
}
