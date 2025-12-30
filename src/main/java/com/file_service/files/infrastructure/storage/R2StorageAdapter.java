package com.file_service.files.infrastructure.storage;

import com.file_service.files.domain.ports.StoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;

public class R2StorageAdapter implements StoragePort {

    private static final Logger log = LoggerFactory.getLogger(R2StorageAdapter.class);

    private final S3Presigner presigner;
    private final S3Client s3Client;
    private final Duration expiry;
    private final String publicBaseUrl;

    public R2StorageAdapter(
            S3Presigner presigner,
            S3Client s3Client,
            Duration expiry,
            String publicBaseUrl
    ) {
        this.presigner = presigner;
        this.s3Client = s3Client;
        this.expiry = expiry;
        this.publicBaseUrl = publicBaseUrl;

        validateConfiguration();
    }

    private void validateConfiguration() {
        if (presigner == null) {
            throw new IllegalArgumentException("S3Presigner cannot be null");
        }
        if (s3Client == null) {
            throw new IllegalArgumentException("S3Client cannot be null");
        }
        if (expiry == null || expiry.isNegative() || expiry.isZero()) {
            throw new IllegalArgumentException("Expiry duration must be positive");
        }
        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            log.warn("Public base URL is not configured. Public URL resolution may fail.");
        }
    }

    @Override
    public PresignedPut presignPut(PresignPutRequest request) {
        validatePresignRequest(request);

        try {
            log.debug("Generating presigned PUT URL: bucket={}, key={}, contentType={}",
                    request.bucket(), request.objectKey(), request.contentType());

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

            log.info("Generated presigned PUT URL: bucket={}, key={}, expiresIn={}",
                    request.bucket(), request.objectKey(), expiry);

            return new PresignedPut(
                    presigned.url().toString(),
                    request.bucket(),
                    request.objectKey(),
                    Map.of("Content-Type", request.contentType())
            );

        } catch (SdkException e) {
            log.error("Failed to generate presigned PUT URL: bucket={}, key={}, error={}",
                    request.bucket(), request.objectKey(), e.getMessage(), e);
            throw new RuntimeException("Failed to generate presigned upload URL", e);
        }
    }

    @Override
    public String resolvePublicUrl(String bucket, String objectKey) {
        validateBucketAndKey(bucket, objectKey);

        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            throw new IllegalStateException(
                    "Public base URL is not configured. Set 'app.r2.publicBaseUrl' in configuration."
            );
        }

        try {
            String base = publicBaseUrl.endsWith("/")
                    ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                    : publicBaseUrl;

            String key = objectKey.startsWith("/")
                    ? objectKey.substring(1)
                    : objectKey;

            String publicUrl = base + "/" + key;

            log.debug("Resolved public URL: bucket={}, key={}, url={}", bucket, objectKey, publicUrl);

            return publicUrl;

        } catch (Exception e) {
            log.error("Failed to resolve public URL: bucket={}, key={}, error={}",
                    bucket, objectKey, e.getMessage(), e);
            throw new RuntimeException("Failed to resolve public URL", e);
        }
    }

    @Override
    public boolean objectExists(String bucket, String objectKey) {
        validateBucketAndKey(bucket, objectKey);

        try {
            log.debug("Checking object existence: bucket={}, key={}", bucket, objectKey);

            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();

            s3Client.headObject(headRequest);

            log.debug("Object exists: bucket={}, key={}", bucket, objectKey);
            return true;

        } catch (NoSuchKeyException e) {
            log.debug("Object does not exist: bucket={}, key={}", bucket, objectKey);
            return false;

        } catch (SdkException e) {
            log.error("Failed to check object existence: bucket={}, key={}, error={}",
                    bucket, objectKey, e.getMessage(), e);
            throw new RuntimeException("Failed to check object existence", e);
        }
    }

    @Override
    public void deleteObject(String bucket, String objectKey) {
        validateBucketAndKey(bucket, objectKey);

        try {
            log.info("Deleting object: bucket={}, key={}", bucket, objectKey);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(deleteRequest);

            log.info("Object deleted successfully: bucket={}, key={}", bucket, objectKey);

        } catch (SdkException e) {
            log.error("Failed to delete object: bucket={}, key={}, error={}",
                    bucket, objectKey, e.getMessage(), e);
            throw new RuntimeException("Failed to delete object from storage", e);
        }
    }

    private void validatePresignRequest(PresignPutRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("PresignPutRequest cannot be null");
        }
        validateBucketAndKey(request.bucket(), request.objectKey());
        if (request.contentType() == null || request.contentType().isBlank()) {
            throw new IllegalArgumentException("Content type cannot be null or blank");
        }
    }

    private void validateBucketAndKey(String bucket, String objectKey) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalArgumentException("Bucket cannot be null or blank");
        }
        if (objectKey == null || objectKey.isBlank()) {
            throw new IllegalArgumentException("Object key cannot be null or blank");
        }
    }
}