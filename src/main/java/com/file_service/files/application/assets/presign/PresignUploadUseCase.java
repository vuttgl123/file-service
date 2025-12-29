package com.file_service.files.application.assets.presign;

import com.file_service.files.domain.model.*;
import com.file_service.files.domain.ports.AssetRepository;
import com.file_service.files.domain.ports.StoragePort;
import com.file_service.shared.application.CommandHandler;
import com.file_service.shared.application.TransactionalUseCase;
import com.file_service.shared.application.UseCase;

import java.time.Clock;
import java.time.LocalDate;

@TransactionalUseCase
public class PresignUploadUseCase implements
        UseCase<PresignUploadCommand, PresignUploadResult>,
        CommandHandler<PresignUploadCommand, PresignUploadResult> {

    private final AssetRepository assetRepository;
    private final StoragePort storagePort;
    private final String bucket;
    private final Clock clock;
    private final PresignUploadValidator validator = new PresignUploadValidator();

    public PresignUploadUseCase(
            AssetRepository assetRepository,
            StoragePort storagePort,
            String bucket,
            Clock clock
    ) {
        this.assetRepository = assetRepository;
        this.storagePort = storagePort;
        this.bucket = bucket;
        this.clock = clock;
    }

    @Override
    public PresignUploadResult execute(PresignUploadCommand request) {
        return handle(request);
    }

    @Override
    public PresignUploadResult handle(PresignUploadCommand command) {
        validator.validate(command);
        AssetType assetType = AssetType.fromValue(command.type());

        String ext = guessExtension(command.contentType(), command.fileName());
        String objectKey = generateObjectKey(assetType, ext);

        ObjectLocation location = ObjectLocation.of(bucket, objectKey);

        if (assetRepository.existsByBucketAndObjectKey(bucket, objectKey)) {
            throw new IllegalStateException("Asset already exists at location: " + location);
        }

        var presigned = storagePort.presignPut(new StoragePort.PresignPutRequest(
                bucket,
                objectKey,
                command.contentType()
        ));

        AssetId assetId = AssetId.generate();
        Asset asset = Asset.initiate(
                assetId,
                assetType,
                StorageProvider.getDefault(),
                location,
                presigned.uploadUrl()
        );

        assetRepository.save(asset);

        return new PresignUploadResult(
                assetId.getValue(),
                presigned.uploadUrl(),
                presigned.bucket(),
                presigned.objectKey(),
                presigned.requiredHeaders()
        );
    }

    private String generateObjectKey(AssetType assetType, String extension) {
        LocalDate date = LocalDate.now(clock);
        String typeFolder = assetType.getValue();
        String year = String.valueOf(date.getYear());
        String month = padZero(date.getMonthValue());
        String uuid = AssetId.generate().getValue();
        String suffix = (extension == null || extension.isBlank()) ? "" : ("." + sanitize(extension));

        return String.format("assets/%s/%s/%s/%s%s", typeFolder, year, month, uuid, suffix);
    }

    private String guessExtension(String contentType, String fileName) {
        if (fileName != null && fileName.contains(".")) {
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (ext.length() <= 10) {
                return ext;
            }
        }

        return switch (contentType) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            case "image/gif" -> "gif";
            case "video/mp4" -> "mp4";
            case "video/webm" -> "webm";
            case "video/quicktime" -> "mov";
            default -> null;
        };
    }

    private String sanitize(String s) {
        return s == null ? "" : s.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "");
    }

    private String padZero(int value) {
        return value < 10 ? ("0" + value) : String.valueOf(value);
    }
}