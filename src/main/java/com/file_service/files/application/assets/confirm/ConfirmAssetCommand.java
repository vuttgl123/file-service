package com.file_service.files.application.assets.confirm;

import com.file_service.shared.application.Command;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Asset confirmation request with metadata")
public record ConfirmAssetCommand(
        @Schema(description = "Asset ID from presign response", example = "550e8400-e29b-41d4-a716-446655440000")
        String assetId,

        @Schema(description = "Bucket name from presign response", example = "my-assets")
        String bucket,

        @Schema(description = "Object key from presign response", example = "assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png")
        String objectKey,

        @Schema(description = "MIME type of the uploaded file", example = "image/png")
        String mimeType,

        @Schema(description = "File size in bytes", example = "123456")
        Long sizeBytes,

        @Schema(description = "Image width in pixels (optional)", example = "1920", nullable = true)
        Integer width,

        @Schema(description = "Image height in pixels (optional)", example = "1080", nullable = true)
        Integer height,

        @Schema(description = "Video duration in seconds (optional)", example = "10.5", nullable = true)
        Double durationSec,

        @Schema(description = "File checksum (optional)", example = "sha256:abcdef123456", nullable = true)
        String checksum
) implements Command {
}