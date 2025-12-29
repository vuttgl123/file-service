package com.file_service.files.infrastructure.config;

import com.file_service.files.domain.ports.StoragePort;
import com.file_service.files.infrastructure.storage.R2StorageAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;

@Configuration
public class StorageConfig {

    @Bean
    public S3Presigner r2Presigner(
            @Value("${app.r2.endpoint}") String endpoint,
            @Value("${app.r2.accessKey}") String accessKey,
            @Value("${app.r2.secretKey}") String secretKey
    ) {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                )
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Bean
    public StoragePort storagePort(
            S3Presigner r2Presigner,
            @Value("${app.r2.presignPutExpirySeconds:600}") long expirySeconds
    ) {
        return new R2StorageAdapter(r2Presigner, Duration.ofSeconds(expirySeconds));
    }
}
