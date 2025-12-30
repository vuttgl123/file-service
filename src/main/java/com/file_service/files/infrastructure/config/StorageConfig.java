package com.file_service.files.infrastructure.config;

import com.file_service.files.domain.ports.StoragePort;
import com.file_service.files.infrastructure.storage.R2StorageAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;

@Configuration
public class StorageConfig {

    @Value("${app.r2.accountId}")
    private String accountId;

    @Value("${app.r2.accessKeyId}")
    private String accessKeyId;

    @Value("${app.r2.secretAccessKey}")
    private String secretAccessKey;

    @Value("${app.r2.bucket}")
    private String bucket;

    @Value("${app.r2.publicBaseUrl}")
    private String publicBaseUrl;

    @Value("${app.r2.presignExpiry:PT15M}")
    private Duration presignExpiry;

    @Bean
    public S3Client s3Client() {
        String endpoint = String.format("https://%s.r2.cloudflarestorage.com", accountId);

        return S3Client.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        String endpoint = String.format("https://%s.r2.cloudflarestorage.com", accountId);

        return S3Presigner.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .build();
    }

    @Bean
    public StoragePort storagePort(S3Presigner presigner, S3Client s3Client) {
        return new R2StorageAdapter(
                presigner,
                s3Client,
                presignExpiry,
                publicBaseUrl
        );
    }
}