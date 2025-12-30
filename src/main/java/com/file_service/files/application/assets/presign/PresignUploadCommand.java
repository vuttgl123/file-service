package com.file_service.files.application.assets.presign;

import com.file_service.shared.application.Command;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to generate presigned upload URL")
public record PresignUploadCommand(
        @Schema(description = "Asset type", example = "image", allowableValues = {"image", "video"})
        String type,

        @Schema(description = "MIME type of the file", example = "image/png")
        String contentType,

        @Schema(description = "Original filename (optional)", example = "photo.png", nullable = true)
        String fileName
) implements Command {
}