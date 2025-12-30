package com.file_service.files.infrastructure.config;

import com.file_service.files.application.assets.confirm.ConfirmAssetUseCase;
import com.file_service.files.application.assets.delete.DeleteAssetUseCase;
import com.file_service.files.application.assets.presign.PresignUploadUseCase;
import com.file_service.files.application.assets.query.GetAssetUrlUseCase;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.domain.ports.StoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public PresignUploadUseCase presignUploadUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort,
            Clock clock,
            @Value("${app.r2.bucket}") String bucket
    ) {
        return new PresignUploadUseCase(
                assetRepository,
                storagePort,
                bucket,
                clock
        );
    }

    @Bean
    public ConfirmAssetUseCase confirmAssetUseCase(
            AssetRepository assetRepository
    ) {
        return new ConfirmAssetUseCase(assetRepository);
    }

    @Bean
    public GetAssetUrlUseCase getAssetUrlUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort
    ) {
        return new GetAssetUrlUseCase(
                assetRepository,
                storagePort
        );
    }

    @Bean
    public DeleteAssetUseCase deleteAssetUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort
    ) {
        return new DeleteAssetUseCase(
                assetRepository,
                storagePort
        );
    }
}